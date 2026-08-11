package com.offerbound.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AIAnalysisRequest {

    @JsonProperty("resume_id")
    private Long resumeId;

    @JsonProperty("resume_text")
    private String resumeText;

    @JsonProperty("job_description")
    private String jobDescription;

    public AIAnalysisRequest() {
    }

    public AIAnalysisRequest(
            Long resumeId,
            String resumeText,
            String jobDescription
    ) {
        this.resumeId = resumeId;
        this.resumeText = resumeText;
        this.jobDescription = jobDescription;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}

