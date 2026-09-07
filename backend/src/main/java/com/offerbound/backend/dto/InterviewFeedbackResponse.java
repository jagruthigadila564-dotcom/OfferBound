package com.offerbound.backend.dto;

import java.util.List;

public class InterviewFeedbackResponse {

    private int overall_score;
    private int communication_score;
    private int technical_score;
    private List<String> strengths;
    private List<String> improvements;
    private List<QuestionFeedback> question_feedback;
    private String summary;

    public InterviewFeedbackResponse() {
    }

    public int getOverall_score() {
        return overall_score;
    }

    public void setOverall_score(int overall_score) {
        this.overall_score = overall_score;
    }

    public int getCommunication_score() {
        return communication_score;
    }

    public void setCommunication_score(int communication_score) {
        this.communication_score = communication_score;
    }

    public int getTechnical_score() {
        return technical_score;
    }

    public void setTechnical_score(int technical_score) {
        this.technical_score = technical_score;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getImprovements() {
        return improvements;
    }

    public void setImprovements(List<String> improvements) {
        this.improvements = improvements;
    }

    public List<QuestionFeedback> getQuestion_feedback() {
        return question_feedback;
    }

    public void setQuestion_feedback(List<QuestionFeedback> question_feedback) {
        this.question_feedback = question_feedback;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static class QuestionFeedback {
        private String question;
        private String feedback;

        public QuestionFeedback() {
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
    }
}