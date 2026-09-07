package com.offerbound.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class AIAnalysisResponse {

    private int ats_score;

    private List<String> matched_skills =
            new ArrayList<>();

    private List<String> missing_skills =
            new ArrayList<>();

    private List<String> strengths =
            new ArrayList<>();

    private List<String> weaknesses =
            new ArrayList<>();

    private List<String> suggestions =
            new ArrayList<>();

    /*
     * Internal fields.
     *
     * Gemini fills these.
     * Spring Boot uses them for deterministic
     * comparison.
     *
     * They are cleared before the response is
     * returned to the frontend.
     */

    private List<String> resume_skills =
            new ArrayList<>();

    private List<String> job_skills =
            new ArrayList<>();

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

    public void setMatched_skills(
            List<String> matched_skills
    ) {
        this.matched_skills =
                matched_skills;
    }

    public List<String> getMissing_skills() {
        return missing_skills;
    }

    public void setMissing_skills(
            List<String> missing_skills
    ) {
        this.missing_skills =
                missing_skills;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(
            List<String> strengths
    ) {
        this.strengths =
                strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(
            List<String> weaknesses
    ) {
        this.weaknesses =
                weaknesses;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(
            List<String> suggestions
    ) {
        this.suggestions =
                suggestions;
    }

    public List<String> getResume_skills() {
        return resume_skills;
    }

    public void setResume_skills(
            List<String> resume_skills
    ) {
        this.resume_skills =
                resume_skills;
    }

    public List<String> getJob_skills() {
        return job_skills;
    }

    public void setJob_skills(
            List<String> job_skills
    ) {
        this.job_skills =
                job_skills;
    }
}