package com.offerbound.backend.controller;

import com.offerbound.backend.dto.TailoredResumeRequest;
import com.offerbound.backend.dto.TailoredResumeResponse;
import com.offerbound.backend.service.TailoredResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin("*")
public class TailoredResumeController {

    private final TailoredResumeService tailoredResumeService;

    public TailoredResumeController(
            TailoredResumeService tailoredResumeService
    ) {
        this.tailoredResumeService = tailoredResumeService;
    }

    @PostMapping("/tailor")
    public ResponseEntity<TailoredResumeResponse> tailorResume(
            @RequestBody TailoredResumeRequest request
    ) {

        TailoredResumeResponse response =
                tailoredResumeService.tailorResume(
                        request.getResumeId(),
                        request.getJobDescription()
                );

        return ResponseEntity.ok(response);
    }
}