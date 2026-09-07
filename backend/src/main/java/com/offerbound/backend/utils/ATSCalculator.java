package com.offerbound.backend.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ATSCalculator {

    public int calculateScore(
            List<String> resumeSkills,
            List<String> jobSkills
    ) {

        if (jobSkills == null || jobSkills.isEmpty()) {
            return 0;
        }

        if (resumeSkills == null) {
            resumeSkills = new ArrayList<>();
        }

        int matched = 0;

        for (String jobSkill : jobSkills) {

            boolean found = false;

            for (String resumeSkill : resumeSkills) {

                if (normalize(resumeSkill)
                        .equals(normalize(jobSkill))) {

                    found = true;
                    break;
                }
            }

            if (found) {
                matched++;
            }
        }

        return Math.round(
                (matched * 100.0f) / jobSkills.size()
        );
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .toLowerCase(Locale.ROOT)
                .replace("&", " and ")
                .replaceAll("[^a-z0-9+#. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}