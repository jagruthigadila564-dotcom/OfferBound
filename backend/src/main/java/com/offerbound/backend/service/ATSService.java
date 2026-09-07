// backend/src/main/java/com/offerbound/backend/service/ATSService.java

package com.offerbound.backend.service;

import com.offerbound.backend.utils.ATSCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ATSService {

    private final ATSCalculator atsCalculator;

    public ATSService(
            ATSCalculator atsCalculator
    ) {
        this.atsCalculator = atsCalculator;
    }

    public List<String> findMatchedSkills(
            List<String> resumeSkills,
            List<String> jobSkills
    ) {

        List<String> matched =
                new ArrayList<>();

        if (resumeSkills == null ||
                jobSkills == null) {

            return matched;
        }

        for (String jobSkill : jobSkills) {

            for (String resumeSkill : resumeSkills) {

                if (normalize(resumeSkill)
                        .equals(normalize(jobSkill))) {

                    if (!containsNormalized(
                            matched,
                            jobSkill
                    )) {

                        matched.add(jobSkill);
                    }

                    break;
                }
            }
        }

        return matched;
    }

    public List<String> findMissingSkills(
            List<String> resumeSkills,
            List<String> jobSkills
    ) {

        List<String> missing =
                new ArrayList<>();

        if (jobSkills == null) {
            return missing;
        }

        if (resumeSkills == null) {
            resumeSkills =
                    new ArrayList<>();
        }

        for (String jobSkill : jobSkills) {

            boolean found = false;

            for (String resumeSkill : resumeSkills) {

                if (normalize(resumeSkill)
                        .equals(normalize(jobSkill))) {

                    found = true;
                    break;
                }
            }

            if (!found &&
                    !containsNormalized(
                            missing,
                            jobSkill
                    )) {

                missing.add(jobSkill);
            }
        }

        return missing;
    }

    public int calculateScore(
            List<String> resumeSkills,
            List<String> jobSkills
    ) {

        return atsCalculator.calculateScore(
                resumeSkills,
                jobSkills
        );
    }

    private boolean containsNormalized(
            List<String> values,
            String target
    ) {

        for (String value : values) {

            if (normalize(value)
                    .equals(normalize(target))) {

                return true;
            }
        }

        return false;
    }

    private String normalize(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .toLowerCase(Locale.ROOT)
                .replace("&", " and ")
                .replaceAll(
                        "[^a-z0-9+#. ]",
                        " "
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }
}