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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AIAnalysisService {

    private final RestTemplate restTemplate;
    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final PDFTextExtractorService pdfTextExtractorService;
    private final ATSService atsService;

    @Value("${ai.service.url:http://127.0.0.1:8000}")
    private String aiServiceUrl;

    public AIAnalysisService(
            RestTemplate restTemplate,
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository,
            PDFTextExtractorService pdfTextExtractorService,
            ATSService atsService
    ) {

        this.restTemplate = restTemplate;
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.pdfTextExtractorService =
                pdfTextExtractorService;
        this.atsService = atsService;
    }

    public AIAnalysisResponse analyzeResume(
            Long resumeId,
            String jobDescription
    ) {

        if (resumeId == null) {
            throw new IllegalArgumentException(
                    "Resume ID cannot be null"
            );
        }

        if (jobDescription == null ||
                jobDescription.isBlank()) {

            throw new IllegalArgumentException(
                    "Job description cannot be empty"
            );
        }

        // ==========================================
        // 1. FIND RESUME
        // ==========================================

        Resume resume =
                resumeRepository.findById(resumeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found with id: "
                                                + resumeId
                                )
                        );

        // ==========================================
        // 2. GET PDF PATH
        // ==========================================

        String filePath =
                resume.getFilePath();

        if (filePath == null ||
                filePath.isBlank()) {

            throw new RuntimeException(
                    "Resume file path is missing"
            );
        }

        // ==========================================
        // 3. EXTRACT RESUME TEXT
        // ==========================================

        String resumeText;

        try {

            resumeText =
                    pdfTextExtractorService
                            .extractText(filePath);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to extract text from resume: "
                            + e.getMessage(),
                    e
            );
        }

        if (resumeText == null ||
                resumeText.isBlank()) {

            throw new RuntimeException(
                    "Could not extract any text from resume"
            );
        }

        // ==========================================
        // 4. SEND TEXT TO AI SERVICE
        // ==========================================

        AIAnalysisRequest request =
                new AIAnalysisRequest(
                        null,
                        resumeText,
                        jobDescription
                );

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<AIAnalysisRequest> entity =
                new HttpEntity<>(
                        request,
                        headers
                );

        String url =
                aiServiceUrl + "/analyze";

        AIAnalysisResponse response =
                restTemplate.postForObject(
                        url,
                        entity,
                        AIAnalysisResponse.class
                );

        if (response == null) {

            throw new RuntimeException(
                    "AI service returned an empty response"
            );
        }

        /*
         * IMPORTANT:
         *
         * Gemini is NOT trusted to calculate:
         *
         * - ATS score
         * - matched skills
         * - missing skills
         *
         * Those values are calculated below.
         */

        List<String> resumeSkills =
                response.getResume_skills() != null
                        ? response.getResume_skills()
                        : new ArrayList<>();

        List<String> jobSkills =
                response.getJob_skills() != null
                        ? response.getJob_skills()
                        : new ArrayList<>();

        // ==========================================
        // 5. DETERMINISTIC COMPARISON
        // ==========================================

        List<String> matchedSkills =
                atsService.findMatchedSkills(
                        resumeSkills,
                        jobSkills
                );

        List<String> missingSkills =
                atsService.findMissingSkills(
                        resumeSkills,
                        jobSkills
                );

        int atsScore =
                atsService.calculateScore(
                        resumeSkills,
                        jobSkills
                );

        // ==========================================
        // 6. SET FINAL ATS RESULT
        // ==========================================

        response.setAts_score(atsScore);

        response.setMatched_skills(
                matchedSkills
        );

        response.setMissing_skills(
                missingSkills
        );

        // ==========================================
        // 7. SAVE TO DATABASE
        // ==========================================

        Analysis analysis =
                new Analysis();

        analysis.setResume(resume);

        analysis.setJobDescription(
                jobDescription
        );

        analysis.setAtsScore(
                atsScore
        );

        analysis.setMatchedSkills(
                String.join(
                        ", ",
                        matchedSkills
                )
        );

        analysis.setMissingSkills(
                String.join(
                        ", ",
                        missingSkills
                )
        );

        analysis.setStrengths(
                response.getStrengths() != null
                        ? String.join(
                                ", ",
                                response.getStrengths()
                        )
                        : ""
        );

        analysis.setWeaknesses(
                response.getWeaknesses() != null
                        ? String.join(
                                ", ",
                                response.getWeaknesses()
                        )
                        : ""
        );

        analysis.setSuggestions(
                response.getSuggestions() != null
                        ? String.join(
                                ", ",
                                response.getSuggestions()
                        )
                        : ""
        );

        analysisRepository.save(
                analysis
        );

        // ==========================================
        // 8. REMOVE INTERNAL EXTRACTION DATA
        // ==========================================

        response.setResume_skills(
                Collections.emptyList()
        );

        response.setJob_skills(
                Collections.emptyList()
        );

        // ==========================================
        // 9. RETURN FINAL RESULT
        // ==========================================

        return response;
    }
}