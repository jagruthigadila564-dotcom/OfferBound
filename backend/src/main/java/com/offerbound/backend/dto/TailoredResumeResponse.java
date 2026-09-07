// backend/src/main/java/com/offerbound/backend/dto/TailoredResumeResponse.java

package com.offerbound.backend.dto;

import java.util.List;

public class TailoredResumeResponse {

    private String professional_summary;

    private List<String> skills;

    private List<Experience> experience;

    private List<Project> projects;

    private List<Education> education;

    private List<String> certifications;


    public TailoredResumeResponse() {
    }


    public String getProfessional_summary() {
        return professional_summary;
    }

    public void setProfessional_summary(
            String professional_summary
    ) {
        this.professional_summary = professional_summary;
    }


    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }


    public List<Experience> getExperience() {
        return experience;
    }

    public void setExperience(
            List<Experience> experience
    ) {
        this.experience = experience;
    }


    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(
            List<Project> projects
    ) {
        this.projects = projects;
    }


    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(
            List<Education> education
    ) {
        this.education = education;
    }


    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(
            List<String> certifications
    ) {
        this.certifications = certifications;
    }


    public static class Experience {

        private String title;
        private String company;
        private String duration;
        private List<String> description;


        public Experience() {
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }


        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }


        public List<String> getDescription() {
            return description;
        }

        public void setDescription(
                List<String> description
        ) {
            this.description = description;
        }
    }


    public static class Project {

        private String name;
        private List<String> technologies;
        private List<String> description;


        public Project() {
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public List<String> getTechnologies() {
            return technologies;
        }

        public void setTechnologies(
                List<String> technologies
        ) {
            this.technologies = technologies;
        }


        public List<String> getDescription() {
            return description;
        }

        public void setDescription(
                List<String> description
        ) {
            this.description = description;
        }
    }


    public static class Education {

        private String degree;
        private String institution;
        private String year;
        private String details;


        public Education() {
        }


        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }


        public String getInstitution() {
            return institution;
        }

        public void setInstitution(
                String institution
        ) {
            this.institution = institution;
        }


        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }


        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }
}