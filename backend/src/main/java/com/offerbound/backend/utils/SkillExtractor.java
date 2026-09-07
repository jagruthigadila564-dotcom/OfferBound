package com.offerbound.backend.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class SkillExtractor {

    /*
     * This class is now used only as a fallback.
     *
     * The main ATS analysis gets skills directly from the AI extraction
     * response and performs deterministic comparison.
     */

    private static final List<String> COMMON_SKILLS = List.of(

            "Java",
            "Spring Boot",
            "Python",
            "C",
            "C++",
            "C#",
            "JavaScript",
            "TypeScript",

            "React",
            "Angular",
            "Vue",

            "HTML",
            "CSS",

            "SQL",
            "MySQL",
            "PostgreSQL",
            "MongoDB",

            "Git",
            "GitHub",

            "Docker",
            "Kubernetes",

            "AWS",
            "Azure",
            "GCP",

            "REST API",
            "REST APIs",
            "Microservices",

            "Hibernate",
            "JPA",

            "TensorFlow",
            "PyTorch",

            "Machine Learning",
            "Deep Learning",
            "Natural Language Processing",
            "NLP",

            "FastAPI",
            "Flask",

            "Problem Solving",
            "Software Fundamentals",
            "Object-Oriented Programming",
            "OOP",

            "Data Structures",
            "Data Structures and Algorithms",
            "Algorithms",

            "Operating Systems",
            "Computer Networks",
            "Database Management",
            "System Design",

            "Communication",
            "Teamwork",
            "Leadership"
    );

    public List<String> extractSkills(
            String resumeText
    ) {

        Set<String> foundSkills =
                new LinkedHashSet<>();

        if (resumeText == null ||
                resumeText.isBlank()) {

            return new ArrayList<>();
        }

        String text =
                resumeText.toLowerCase(Locale.ROOT);

        for (String skill : COMMON_SKILLS) {

            if (text.contains(
                    skill.toLowerCase(Locale.ROOT)
            )) {

                foundSkills.add(skill);
            }
        }

        return new ArrayList<>(
                foundSkills
        );
    }
}