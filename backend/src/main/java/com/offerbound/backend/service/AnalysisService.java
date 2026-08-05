package com.offerbound.backend.service;

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

    public AnalysisService(
            ResumeParserService resumeParserService,
            SkillExtractor skillExtractor,
            ATSCalculator atsCalculator,
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository
    ) {
        this.resumeParserService = resumeParserService;
        this.skillExtractor = skillExtractor;
        this.atsCalculator = atsCalculator;
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
    }

    public AnalysisResponse analyzeResume(
            AnalysisRequest request
    ) throws IOException {

        Resume resumeEntity = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ResumeTextResponse resume =
                resumeParserService.extractResumeText(request.getResumeId());

        String resumeText = resume.getExtractedText();

        List<String> resumeSkills =
                skillExtractor.extractSkills(resumeText);

        List<String> jdSkills =
                skillExtractor.extractSkills(request.getJobDescription());

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String skill : jdSkills) {

            if (resumeSkills.contains(skill)) {
                matchedSkills.add(skill);
            } else {
                missingSkills.add(skill);
            }
        }

        int atsScore = atsCalculator.calculateScore(
                resumeSkills,
                jdSkills
        );

        Analysis analysis = new Analysis();

        analysis.setResume(resumeEntity);
        analysis.setJobDescription(request.getJobDescription());
        analysis.setAtsScore(atsScore);
        analysis.setMatchedSkills(String.join(", ", matchedSkills));
        analysis.setMissingSkills(String.join(", ", missingSkills));

        analysisRepository.save(analysis);

        return new AnalysisResponse(
                atsScore,
                matchedSkills,
                missingSkills
        );
    }

    public List<AnalysisHistoryResponse> getAnalysisHistory(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        List<Analysis> analyses = analysisRepository.findByResume(resume);

        List<AnalysisHistoryResponse> response = new ArrayList<>();

        for (Analysis analysis : analyses) {

            response.add(new AnalysisHistoryResponse(
                    analysis.getId(),
                    analysis.getAtsScore(),
                    analysis.getCreatedAt()
            ));

        }

        return response;
    }
}