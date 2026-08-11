package com.offerbound.backend.dto;

import java.util.List;

public class AIAnalysisResponse {

    private int ats_score;

    private List<String> matched_skills;

    private List<String> missing_skills;

    private List<String> strengths;

    private List<String> weaknesses;

    private List<String> suggestions;

    public AIAnalysisResponse() {
    }

    public int getAts_score() {
        return ats_score;
    }

    public void setAts_score(int ats_score) {
        this.ats_score = ats_score;
    }

    public List<String> getMatched_skills() {
        return matched_skills;
    }

    public void setMatched_skills(List<String> matched_skills) {
        this.matched_skills = matched_skills;
    }

    public List<String> getMissing_skills() {
        return missing_skills;
    }

    public void setMissing_skills(List<String> missing_skills) {
        this.missing_skills = missing_skills;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}