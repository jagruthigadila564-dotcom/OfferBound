package com.offerbound.backend.dto;

import java.util.List;

public class InterviewStartResponse {

    private List<String> questions;

    public InterviewStartResponse() {
    }

    public InterviewStartResponse(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}