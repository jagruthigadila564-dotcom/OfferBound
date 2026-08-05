package com.offerbound.backend.dto;

import java.time.LocalDateTime;

public class AnalysisHistoryResponse {

    private Long id;
    private int atsScore;
    private LocalDateTime createdAt;

    public AnalysisHistoryResponse() {
    }

    public AnalysisHistoryResponse(
            Long id,
            int atsScore,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.atsScore = atsScore;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public int getAtsScore() {
        return atsScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}