package com.offerbound.backend.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ATSCalculator {

    public int calculateScore(
            List<String> resumeSkills,
            List<String> jobSkills
    ) {
        if (jobSkills.isEmpty()) {
            return 0;
        }

        int matched = 0;

        for (String skill : jobSkills) {
            if (resumeSkills.contains(skill)) {
                matched++;
            }
        }

        return (matched * 100) / jobSkills.size();
    }
}
