package com.offerbound.backend.controller;

import com.offerbound.backend.dto.ResumeResponse;
import com.offerbound.backend.dto.ResumeTextResponse;
import com.offerbound.backend.service.ResumeParserService;
import com.offerbound.backend.service.ResumeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin("*")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeParserService resumeParserService;

    public ResumeController(
            ResumeService resumeService,
            ResumeParserService resumeParserService
    ) {
        this.resumeService = resumeService;
        this.resumeParserService = resumeParserService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResumeResponse uploadResume(
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return resumeService.uploadResume(userId, file);
    }

    @GetMapping("/{resumeId}/extract")
    public ResumeTextResponse extractResumeText(
            @PathVariable Long resumeId
    ) throws IOException {
        return resumeParserService.extractResumeText(resumeId);
    }

    @GetMapping("/user/{userId}")
    public List<ResumeResponse> getUserResumes(
            @PathVariable Long userId
    ) {
        return resumeService.getUserResumes(userId);
    }

    @DeleteMapping("/{resumeId}")
    public String deleteResume(
            @PathVariable Long resumeId
    ) throws IOException {
        return resumeService.deleteResume(resumeId);
    }
}