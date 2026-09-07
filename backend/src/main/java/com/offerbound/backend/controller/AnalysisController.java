package com.offerbound.backend.controller;

import com.offerbound.backend.dto.AnalysisHistoryResponse;
import com.offerbound.backend.dto.AnalysisResponse;
import com.offerbound.backend.service.AnalysisService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin("*")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(
            AnalysisService analysisService
    ) {
        this.analysisService = analysisService;
    }

    @PostMapping
    public AnalysisResponse analyzeResume(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobDescription") String jobDescription
    ) throws IOException {

        return analysisService.analyzeResume(
                resume,
                jobDescription
        );
    }

    @GetMapping("/history/{resumeId}")
    public List<AnalysisHistoryResponse> getHistory(
            @PathVariable Long resumeId
    ) {

        return analysisService.getAnalysisHistory(resumeId);
    }
}