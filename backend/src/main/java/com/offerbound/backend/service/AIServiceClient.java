package com.offerbound.backend.service;

import com.offerbound.backend.dto.AIAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AIServiceClient {

    private final RestClient restClient;

    public AIServiceClient(
            @Value("${ai.service.url:http://127.0.0.1:8000}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public AIAnalysisResponse analyzeResume(
            String resumeText,
            String jobDescription
    ) {
        return analyze(resumeText, jobDescription);
    }

    public AIAnalysisResponse analyze(
            String resumeText,
            String jobDescription
    ) {

        return restClient.post()
                .uri("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AIAnalysisRequest(
                        resumeText,
                        jobDescription
                ))
                .retrieve()
                .body(AIAnalysisResponse.class);
    }

    private record AIAnalysisRequest(
            String resume_text,
            String job_description
    ) {
    }
}