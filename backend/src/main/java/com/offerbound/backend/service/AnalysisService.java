package com.offerbound.backend.service;

import com.offerbound.backend.dto.AIAnalysisResponse;
import com.offerbound.backend.dto.AnalysisHistoryResponse;
import com.offerbound.backend.dto.AnalysisRequest;
import com.offerbound.backend.dto.AnalysisResponse;
import com.offerbound.backend.dto.ResumeTextResponse;
import com.offerbound.backend.entity.Analysis;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.repository.AnalysisRepository;
import com.offerbound.backend.repository.ResumeRepository;
import com.offerbound.backend.utils.ATSCalculator;
import com.offerbound.backend.utils.SkillExtractor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisService {

    private final ResumeParserService resumeParserService;
    private final SkillExtractor skillExtractor;
    private final ATSCalculator atsCalculator;
    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final AIServiceClient aiServiceClient;

    public AnalysisService(
            ResumeParserService resumeParserService,
            SkillExtractor skillExtractor,
            ATSCalculator atsCalculator,
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository,
            AIServiceClient aiServiceClient
    ) {
        this.resumeParserService = resumeParserService;
        this.skillExtractor = skillExtractor;
        this.atsCalculator = atsCalculator;
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.aiServiceClient = aiServiceClient;
    }

    public AnalysisResponse analyzeResume(
            AnalysisRequest request
    ) throws IOException {

        // 1. Find resume
        Resume resumeEntity = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // 2. Extract resume text
        ResumeTextResponse resume =
                resumeParserService.extractResumeText(request.getResumeId());

        String resumeText = resume.getExtractedText();

        // 3. Send resume + JD to FastAPI → Gemini
        AIAnalysisResponse aiResponse =
                aiServiceClient.analyze(
                        resumeText,
                        request.getJobDescription()
                );

        // 4. Create analysis record
        Analysis analysis = new Analysis();

        analysis.setResume(resumeEntity);
        analysis.setJobDescription(request.getJobDescription());

        analysis.setAtsScore(
                aiResponse.getAts_score()
        );

        analysis.setMatchedSkills(
                String.join(
                        ", ",
                        aiResponse.getMatched_skills()
                )
        );

        analysis.setMissingSkills(
                String.join(
                        ", ",
                        aiResponse.getMissing_skills()
                )
        );

        // 5. Save analysis in MySQL
        analysisRepository.save(analysis);

        // 6. Return response to frontend/Postman
        return new AnalysisResponse(
                aiResponse.getAts_score(),
                aiResponse.getMatched_skills(),
                aiResponse.getMissing_skills()
        );
    }

    public List<AnalysisHistoryResponse> getAnalysisHistory(
            Long resumeId
    ) {

        // Find resume
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // Get previous analyses
        List<Analysis> analyses =
                analysisRepository.findByResume(resume);

        List<AnalysisHistoryResponse> response =
                new ArrayList<>();

        // Convert entities to response DTO
        for (Analysis analysis : analyses) {

            response.add(
                    new AnalysisHistoryResponse(
                            analysis.getId(),
                            analysis.getAtsScore(),
                            analysis.getCreatedAt()
                    )
            );
        }

        return response;
    }
}