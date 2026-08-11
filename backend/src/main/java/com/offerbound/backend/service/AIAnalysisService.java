package com.offerbound.backend.service;

import com.offerbound.backend.dto.AIAnalysisRequest;
import com.offerbound.backend.dto.AIAnalysisResponse;
import com.offerbound.backend.entity.Analysis;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.repository.AnalysisRepository;
import com.offerbound.backend.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIAnalysisService {

    private final RestTemplate restTemplate;
    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final PDFTextExtractorService pdfTextExtractorService;

    @Value("${ai.service.url:http://127.0.0.1:8000}")
    private String aiServiceUrl;

    public AIAnalysisService(
            RestTemplate restTemplate,
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository,
            PDFTextExtractorService pdfTextExtractorService
    ) {
        this.restTemplate = restTemplate;
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.pdfTextExtractorService = pdfTextExtractorService;
    }

    public AIAnalysisResponse analyzeResume(
            Long resumeId,
            String jobDescription
    ) {

        // 1. Find the resume in MySQL
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume not found with id: " + resumeId
                        )
                );

        // 2. Get the uploaded resume file path
        String filePath = resume.getFilePath();

        if (filePath == null || filePath.isBlank()) {
            throw new RuntimeException(
                    "Resume file path is missing"
            );
        }

        // 3. Extract text from the uploaded PDF
        String resumeText;

        try {
            resumeText = pdfTextExtractorService.extractText(filePath);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to extract text from resume: " + e.getMessage(),
                    e
            );
        }

        // 4. Make sure text was extracted
        if (resumeText == null || resumeText.isBlank()) {
            throw new RuntimeException(
                    "Could not extract any text from the resume"
            );
        }

        // 5. Create request for FastAPI
        AIAnalysisRequest request =
                new AIAnalysisRequest(
                        null,
                        resumeText,
                        jobDescription
                );

        // 6. Set JSON headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AIAnalysisRequest> entity =
                new HttpEntity<>(request, headers);

        // 7. Call FastAPI
        String url = aiServiceUrl + "/analyze";

        AIAnalysisResponse response =
                restTemplate.postForObject(
                        url,
                        entity,
                        AIAnalysisResponse.class
                );

        // 8. Make sure FastAPI returned a response
        if (response == null) {
            throw new RuntimeException(
                    "AI service returned an empty response"
            );
        }

        // 9. Create Analysis entity
        Analysis analysis = new Analysis();

        analysis.setResume(resume);
        analysis.setJobDescription(jobDescription);
        analysis.setAtsScore(response.getAts_score());

        // Convert List<String> to String for MySQL TEXT columns
        analysis.setMatchedSkills(
                response.getMatched_skills() != null
                        ? String.join(", ", response.getMatched_skills())
                        : ""
        );

        analysis.setMissingSkills(
                response.getMissing_skills() != null
                        ? String.join(", ", response.getMissing_skills())
                        : ""
        );

        analysis.setStrengths(
                response.getStrengths() != null
                        ? String.join(", ", response.getStrengths())
                        : ""
        );

        analysis.setWeaknesses(
                response.getWeaknesses() != null
                        ? String.join(", ", response.getWeaknesses())
                        : ""
        );

        analysis.setSuggestions(
                response.getSuggestions() != null
                        ? String.join(", ", response.getSuggestions())
                        : ""
        );

        // 10. Save analysis to MySQL
        analysisRepository.save(analysis);

        // 11. Return AI response
        return response;
    }
}