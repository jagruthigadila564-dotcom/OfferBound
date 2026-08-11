package com.offerbound.backend.controller;

import com.offerbound.backend.dto.AIAnalysisRequest;
import com.offerbound.backend.dto.AIAnalysisResponse;
import com.offerbound.backend.service.AIAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin("*")
public class AIAnalysisController {

    private final AIAnalysisService aiAnalysisService;

    public AIAnalysisController(
            AIAnalysisService aiAnalysisService
    ) {
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AIAnalysisResponse> analyzeResume(
            @RequestBody AIAnalysisRequest request
    ) {

        AIAnalysisResponse response =
                aiAnalysisService.analyzeResume(
                        request.getResumeId(),
                        request.getJobDescription()
                );

        return ResponseEntity.ok(response);
    }
}

