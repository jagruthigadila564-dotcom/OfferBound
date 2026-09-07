package com.offerbound.backend.service;

import com.offerbound.backend.dto.*;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterviewService {

    private final RestTemplate restTemplate;
    private final ResumeRepository resumeRepository;
    private final PDFTextExtractorService pdfTextExtractorService;

    @Value("${ai.service.url:http://127.0.0.1:8000}")
    private String aiServiceUrl;

    public InterviewService(
            RestTemplate restTemplate,
            ResumeRepository resumeRepository,
            PDFTextExtractorService pdfTextExtractorService
    ) {
        this.restTemplate = restTemplate;
        this.resumeRepository = resumeRepository;
        this.pdfTextExtractorService = pdfTextExtractorService;
    }

    private String resolveResumeText(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found with id: " + resumeId));

        String filePath = resume.getFilePath();

        if (filePath == null || filePath.isBlank()) {
            throw new RuntimeException("Resume file path is missing");
        }

        try {
            String text = pdfTextExtractorService.extractText(filePath);

            if (text == null || text.isBlank()) {
                throw new RuntimeException("Could not extract any text from the resume");
            }

            return text;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from resume: " + e.getMessage(), e);
        }
    }

    public InterviewStartResponse startInterview(Long resumeId, String jobDescription) {

        String resumeText = resolveResumeText(resumeId);

        Map<String, Object> body = new HashMap<>();
        body.put("resume_text", resumeText);
        body.put("job_description", jobDescription);
        body.put("num_questions", 5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String url = aiServiceUrl + "/interview/start";

        InterviewStartResponse response =
                restTemplate.postForObject(url, entity, InterviewStartResponse.class);

        if (response == null || response.getQuestions() == null || response.getQuestions().isEmpty()) {
            throw new RuntimeException("AI service did not return any interview questions");
        }

        return response;
    }

    public InterviewFeedbackResponse getFeedback(
            Long resumeId,
            String jobDescription,
            List<InterviewQA> transcript
    ) {

        if (transcript == null || transcript.isEmpty()) {
            throw new RuntimeException("No interview answers were submitted");
        }

        String resumeText = resolveResumeText(resumeId);

        Map<String, Object> body = new HashMap<>();
        body.put("resume_text", resumeText);
        body.put("job_description", jobDescription);
        body.put("transcript", transcript);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String url = aiServiceUrl + "/interview/feedback";

        InterviewFeedbackResponse response =
                restTemplate.postForObject(url, entity, InterviewFeedbackResponse.class);

        if (response == null) {
            throw new RuntimeException("AI service returned an empty feedback response");
        }

        return response;
    }
}