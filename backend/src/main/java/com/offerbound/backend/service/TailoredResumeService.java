// backend/src/main/java/com/offerbound/backend/service/TailoredResumeService.java

package com.offerbound.backend.service;

import com.offerbound.backend.dto.TailoredResumeResponse;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.repository.ResumeRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TailoredResumeService {

    private final RestTemplate restTemplate;

    private final ResumeRepository resumeRepository;

    private final PDFTextExtractorService pdfTextExtractorService;


    @Value("${ai.service.url:http://127.0.0.1:8000}")
    private String aiServiceUrl;


    public TailoredResumeService(
            RestTemplate restTemplate,
            ResumeRepository resumeRepository,
            PDFTextExtractorService pdfTextExtractorService
    ) {

        this.restTemplate = restTemplate;

        this.resumeRepository = resumeRepository;

        this.pdfTextExtractorService =
                pdfTextExtractorService;
    }


    public TailoredResumeResponse tailorResume(
            Long resumeId,
            String jobDescription
    ) {

        if (resumeId == null) {

            throw new RuntimeException(
                    "Resume ID cannot be null"
            );
        }


        if (
                jobDescription == null ||
                jobDescription.isBlank()
        ) {

            throw new RuntimeException(
                    "Job description cannot be empty"
            );
        }


        Resume resume =
                resumeRepository
                        .findById(resumeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found with id: "
                                                + resumeId
                                )
                        );


        String filePath =
                resume.getFilePath();


        if (
                filePath == null ||
                filePath.isBlank()
        ) {

            throw new RuntimeException(
                    "Resume file path is missing"
            );
        }


        String resumeText;


        try {

            resumeText =
                    pdfTextExtractorService
                            .extractText(filePath);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to extract resume text: "
                            + e.getMessage(),
                    e
            );
        }


        if (
                resumeText == null ||
                resumeText.isBlank()
        ) {

            throw new RuntimeException(
                    "Could not extract any text from resume"
            );
        }


        TailorAIRequest request =
                new TailorAIRequest(
                        resumeText,
                        jobDescription
                );


        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );


        HttpEntity<TailorAIRequest> entity =
                new HttpEntity<>(
                        request,
                        headers
                );


        String url =
                aiServiceUrl +
                        "/tailor-resume";


        try {

            TailoredResumeResponse response =
                    restTemplate.postForObject(
                            url,
                            entity,
                            TailoredResumeResponse.class
                    );


            if (response == null) {

                throw new RuntimeException(
                        "AI service returned an empty response"
                );
            }


            return response;


        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to get tailored resume from AI service: "
                            + e.getMessage(),
                    e
            );
        }
    }


    private record TailorAIRequest(
            String resumeText,
            String jobDescription
    ) {
    }
}