package com.offerbound.backend.controller;

import com.offerbound.backend.service.InterviewService;
import com.offerbound.backend.service.InterviewService.InterviewFeedbackResponse;
import com.offerbound.backend.service.InterviewService.InterviewQuestionsResponse;
import com.offerbound.backend.service.InterviewService.InterviewQA;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin("*")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(
            InterviewService interviewService
    ) {
        this.interviewService =
                interviewService;
    }

    @PostMapping("/start")
    public ResponseEntity<InterviewQuestionsResponse>
    startInterview(
            @RequestBody StartInterviewRequest request
    ) {

        InterviewQuestionsResponse response =
                interviewService.generateQuestions(
                        request.resumeId(),
                        request.jobDescription()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/feedback")
    public ResponseEntity<InterviewFeedbackResponse>
    getFeedback(
            @RequestBody FeedbackRequest request
    ) {

        InterviewFeedbackResponse response =
                interviewService.generateFeedback(
                        request.resumeId(),
                        request.jobDescription(),
                        request.transcript()
                );

        return ResponseEntity.ok(response);
    }

    public record StartInterviewRequest(
            Long resumeId,
            String jobDescription
    ) {}

    public record FeedbackRequest(
            Long resumeId,
            String jobDescription,
            List<InterviewQA> transcript
    ) {}
}