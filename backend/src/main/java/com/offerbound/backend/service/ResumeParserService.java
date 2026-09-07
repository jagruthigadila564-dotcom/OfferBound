package com.offerbound.backend.service;

import com.offerbound.backend.dto.ResumeTextResponse;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.parser.PdfParser;
import com.offerbound.backend.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeParserService {

    private final ResumeRepository resumeRepository;
    private final PdfParser pdfParser;

    public ResumeParserService(ResumeRepository resumeRepository,
                               PdfParser pdfParser) {

        this.resumeRepository = resumeRepository;
        this.pdfParser = pdfParser;
    }

    public ResumeTextResponse extractResumeText(Long resumeId) throws IOException {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found"));

        String extractedText =
                pdfParser.extractText(resume.getFilePath());

        return new ResumeTextResponse(
                resume.getId(),
                extractedText
        );
    }

    public String extractText(MultipartFile resumeFile) throws IOException {
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        try (PDDocument document = PDDocument.load(resumeFile.getInputStream())) {
            return new PDFTextStripper().getText(document);
        }
    }
}