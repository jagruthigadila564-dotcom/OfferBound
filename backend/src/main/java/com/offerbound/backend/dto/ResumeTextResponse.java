package com.offerbound.backend.dto;

public class ResumeTextResponse {

    private Long resumeId;

    private String extractedText;

    public ResumeTextResponse() {
    }

    public ResumeTextResponse(Long resumeId, String extractedText) {
        this.resumeId = resumeId;
        this.extractedText = extractedText;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }
}