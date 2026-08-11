package com.offerbound.backend.service;

import com.offerbound.backend.dto.AIAnalysisResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AIServiceClient {

    private final RestClient restClient;

    public AIServiceClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://127.0.0.1:8001")
                .build();
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