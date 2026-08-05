package com.offerbound.backend.controller;

import com.offerbound.backend.dto.AnalysisHistoryResponse;
import com.offerbound.backend.dto.AnalysisRequest;
import com.offerbound.backend.dto.AnalysisResponse;
import com.offerbound.backend.service.AnalysisService;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody AnalysisRequest request
    ) throws IOException {

        return analysisService.analyzeResume(request);

    }

    @GetMapping("/history/{resumeId}")
    public List<AnalysisHistoryResponse> getHistory(
            @PathVariable Long resumeId
    ) {

        return analysisService.getAnalysisHistory(resumeId);

    }

}