package com.offerbound.backend.utils;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SkillExtractor {

    private static final List<String> SKILLS = Arrays.asList(

            "Java",
            "Spring Boot",
            "Python",
            "C",
            "C++",
            "JavaScript",
            "React",
            "Angular",
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
            "Microservices",
            "Hibernate",
            "JPA",
            "TensorFlow",
            "PyTorch",
            "Machine Learning",
            "Deep Learning",
            "NLP",
            "FastAPI",
            "Flask"
    );

    public List<String> extractSkills(String resumeText) {

        Set<String> foundSkills = new LinkedHashSet<>();

        String lowerCaseResume = resumeText.toLowerCase();

        for (String skill : SKILLS) {

            if (lowerCaseResume.contains(skill.toLowerCase())) {

                foundSkills.add(skill);

            }

        }

        return new ArrayList<>(foundSkills);

    }

}