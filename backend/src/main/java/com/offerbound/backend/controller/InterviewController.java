package com.offerbound.backend.controller;

import com.offerbound.backend.dto.*;
import com.offerbound.backend.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin("*")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/start")
    public ResponseEntity<InterviewStartResponse> startInterview(
            @RequestBody InterviewStartRequest request
    ) {
        InterviewStartResponse response = interviewService.startInterview(
                request.getResumeId(),
                request.getJobDescription()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/feedback")
    public ResponseEntity<InterviewFeedbackResponse> getFeedback(
            @RequestBody InterviewFeedbackRequest request
    ) {
        InterviewFeedbackResponse response = interviewService.getFeedback(
                request.getResumeId(),
                request.getJobDescription(),
                request.getTranscript()
        );

        return ResponseEntity.ok(response);
    }
}