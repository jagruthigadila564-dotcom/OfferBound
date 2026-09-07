package com.offerbound.backend.dto;

import java.util.List;

public class InterviewFeedbackRequest {

    private Long resumeId;
    private String jobDescription;
    private List<InterviewQA> transcript;

    public InterviewFeedbackRequest() {
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public List<InterviewQA> getTranscript() {
        return transcript;
    }

    public void setTranscript(List<InterviewQA> transcript) {
        this.transcript = transcript;
    }
}