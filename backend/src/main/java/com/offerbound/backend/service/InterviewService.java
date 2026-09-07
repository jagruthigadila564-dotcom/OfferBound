package com.offerbound.backend.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.repository.ResumeRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InterviewService {

    private final RestTemplate restTemplate;
    private final ResumeRepository resumeRepository;
    private final PDFTextExtractorService pdfTextExtractorService;

    @Value("${ai.service.url:http://127.0.0.1:8000}")
    private String aiServiceUrl;

    public InterviewService(
            RestTemplate restTemplate,
            ResumeRepository resumeRepository,
            PDFTextExtractorService pdfTextExtractorService
    ) {
        this.restTemplate = restTemplate;
        this.resumeRepository = resumeRepository;
        this.pdfTextExtractorService = pdfTextExtractorService;
    }

    public InterviewQuestionsResponse generateQuestions(
            Long resumeId,
            String jobDescription
    ) {

        String resumeText = getResumeText(resumeId);

        if (jobDescription == null || jobDescription.isBlank()) {
            throw new RuntimeException(
                    "Job description cannot be empty"
            );
        }

        InterviewQuestionsRequest request =
                new InterviewQuestionsRequest(
                        resumeText,
                        jobDescription,
                        5
                );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<InterviewQuestionsRequest> entity =
                new HttpEntity<>(
                        request,
                        headers
                );

        String url =
                aiServiceUrl + "/interview/questions";

        try {

            InterviewQuestionsResponse response =
                    restTemplate.postForObject(
                            url,
                            entity,
                            InterviewQuestionsResponse.class
                    );

            if (response == null) {

                throw new RuntimeException(
                        "AI service returned empty interview questions"
                );
            }

            return response;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate interview questions: "
                            + e.getMessage(),
                    e
            );
        }
    }

    public InterviewFeedbackResponse generateFeedback(
            Long resumeId,
            String jobDescription,
            List<InterviewQA> transcript
    ) {

        String resumeText = getResumeText(resumeId);

        if (jobDescription == null || jobDescription.isBlank()) {

            throw new RuntimeException(
                    "Job description cannot be empty"
            );
        }

        if (transcript == null || transcript.isEmpty()) {

            throw new RuntimeException(
                    "Interview transcript cannot be empty"
            );
        }

        InterviewFeedbackRequest request =
                new InterviewFeedbackRequest(
                        resumeText,
                        jobDescription,
                        transcript
                );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<InterviewFeedbackRequest> entity =
                new HttpEntity<>(
                        request,
                        headers
                );

        String url =
                aiServiceUrl + "/interview/feedback";

        try {

            InterviewFeedbackResponse response =
                    restTemplate.postForObject(
                            url,
                            entity,
                            InterviewFeedbackResponse.class
                    );

            if (response == null) {

                throw new RuntimeException(
                        "AI service returned empty interview feedback"
                );
            }

            return response;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate interview feedback: "
                            + e.getMessage(),
                    e
            );
        }
    }

    private String getResumeText(Long resumeId) {

        if (resumeId == null) {

            throw new RuntimeException(
                    "Resume ID cannot be null"
            );
        }

        Resume resume =
                resumeRepository.findById(resumeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found with id: "
                                                + resumeId
                                )
                        );

        String filePath =
                resume.getFilePath();

        if (filePath == null || filePath.isBlank()) {

            throw new RuntimeException(
                    "Resume file path is missing"
            );
        }

        try {

            String resumeText =
                    pdfTextExtractorService.extractText(
                            filePath
                    );

            if (resumeText == null ||
                    resumeText.isBlank()) {

                throw new RuntimeException(
                        "Could not extract any text from resume"
                );
            }

            return resumeText;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to extract resume text: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =====================================================
    // REQUEST DTOs
    // =====================================================

    public record InterviewQuestionsRequest(
            @JsonProperty("resumeText")
            String resumeText,

            @JsonProperty("jobDescription")
            String jobDescription,

            @JsonProperty("numQuestions")
            int numQuestions
    ) {}

    public record InterviewFeedbackRequest(
            @JsonProperty("resumeText")
            String resumeText,

            @JsonProperty("jobDescription")
            String jobDescription,

            List<InterviewQA> transcript
    ) {}

    public record InterviewQA(
            String question,
            String answer
    ) {}

    // =====================================================
    // RESPONSE DTOs
    // =====================================================

    public static class InterviewQuestionsResponse {

        private List<String> questions;

        public InterviewQuestionsResponse() {}

        public List<String> getQuestions() {
            return questions;
        }

        public void setQuestions(
                List<String> questions
        ) {
            this.questions = questions;
        }
    }

    public static class InterviewFeedbackResponse {

        private int overall_score;
        private int communication_score;
        private int technical_score;

        private List<String> strengths;
        private List<String> improvements;

        private List<QuestionFeedback>
                question_feedback;

        private String summary;

        public InterviewFeedbackResponse() {}

        public int getOverall_score() {
            return overall_score;
        }

        public void setOverall_score(
                int overall_score
        ) {
            this.overall_score = overall_score;
        }

        public int getCommunication_score() {
            return communication_score;
        }

        public void setCommunication_score(
                int communication_score
        ) {
            this.communication_score =
                    communication_score;
        }

        public int getTechnical_score() {
            return technical_score;
        }

        public void setTechnical_score(
                int technical_score
        ) {
            this.technical_score =
                    technical_score;
        }

        public List<String> getStrengths() {
            return strengths;
        }

        public void setStrengths(
                List<String> strengths
        ) {
            this.strengths = strengths;
        }

        public List<String> getImprovements() {
            return improvements;
        }

        public void setImprovements(
                List<String> improvements
        ) {
            this.improvements = improvements;
        }

        public List<QuestionFeedback>
        getQuestion_feedback() {

            return question_feedback;
        }

        public void setQuestion_feedback(
                List<QuestionFeedback>
                        question_feedback
        ) {

            this.question_feedback =
                    question_feedback;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(
                String summary
        ) {
            this.summary = summary;
        }
    }

    public static class QuestionFeedback {

        private String question;
        private String feedback;

        public QuestionFeedback() {}

        public String getQuestion() {
            return question;
        }

        public void setQuestion(
                String question
        ) {
            this.question = question;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(
                String feedback
        ) {
            this.feedback = feedback;
        }
    }
}