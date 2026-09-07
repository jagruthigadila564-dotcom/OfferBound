package com.offerbound.backend.service;

import com.offerbound.backend.dto.AnalysisHistoryResponse;
import com.offerbound.backend.dto.AnalysisResponse;
import com.offerbound.backend.entity.Analysis;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.repository.AnalysisRepository;
import com.offerbound.backend.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private final ResumeRepository resumeRepository;
    private final AnalysisRepository analysisRepository;
    private final ResumeParserService resumeParserService;
    private final AIServiceClient aiServiceClient;

    public AnalysisService(
            ResumeRepository resumeRepository,
            AnalysisRepository analysisRepository,
            ResumeParserService resumeParserService,
            AIServiceClient aiServiceClient
    ) {
        this.resumeRepository = resumeRepository;
        this.analysisRepository = analysisRepository;
        this.resumeParserService = resumeParserService;
        this.aiServiceClient = aiServiceClient;
    }

    public AnalysisResponse analyzeResume(
            MultipartFile resumeFile,
            String jobDescription
    ) throws IOException {

        // 1. Validate resume
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        // 2. Validate job description
        if (jobDescription == null || jobDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Job description is required");
        }

        // 3. Extract text from PDF
        String resumeText = resumeParserService.extractText(resumeFile);

        if (resumeText == null || resumeText.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Could not extract text from resume"
            );
        }

        // 4. Send resume text + JD to FastAPI/Gemini
        com.offerbound.backend.dto.AIAnalysisResponse aiResponse =
                aiServiceClient.analyzeResume(
                        resumeText,
                        jobDescription
                );

        return new AnalysisResponse(
                aiResponse.getAts_score(),
                aiResponse.getMatched_skills(),
                aiResponse.getMissing_skills()
        );
    }

    public List<AnalysisHistoryResponse> getAnalysisHistory(
            Long resumeId
    ) {

        if (resumeId == null) {
            throw new IllegalArgumentException("Resume ID is required");
        }

        List<Analysis> analyses =
                analysisRepository.findByResumeId(resumeId);

        return analyses.stream()
                .map(this::convertToHistoryResponse)
                .collect(Collectors.toList());
    }

    private AnalysisHistoryResponse convertToHistoryResponse(
            Analysis analysis
    ) {

        AnalysisHistoryResponse response =
                new AnalysisHistoryResponse();

        // Set fields according to your DTO
        response.setId(analysis.getId());
        response.setAtsScore(analysis.getAtsScore());
        response.setCreatedAt(analysis.getCreatedAt());

        return response;
    }
}