package com.offerbound.backend.controller;

import com.offerbound.backend.dto.ResumeResponse;
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

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // Upload Resume
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadResume(
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return resumeService.uploadResume(userId, file);
    }

    // Get All Resumes of a User
    @GetMapping("/user/{userId}")
    public List<ResumeResponse> getUserResumes(
            @PathVariable Long userId
    ) {

        return resumeService.getUserResumes(userId);
    }

    // Delete Resume
    @DeleteMapping("/{resumeId}")
    public String deleteResume(
            @PathVariable Long resumeId
    ) throws IOException {

        return resumeService.deleteResume(resumeId);
    }

}