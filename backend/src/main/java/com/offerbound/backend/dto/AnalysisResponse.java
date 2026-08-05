package com.offerbound.backend.dto;

import java.util.List;

public class AnalysisResponse {

    private int atsScore;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    public AnalysisResponse() {
    }

    public AnalysisResponse(
            int atsScore,
            List<String> matchedSkills,
            List<String> missingSkills
    ) {

        this.atsScore = atsScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;

    }

    public int getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(int atsScore) {
        this.atsScore = atsScore;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

}