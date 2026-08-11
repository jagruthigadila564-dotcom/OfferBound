package com.offerbound.backend.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class PDFTextExtractorService {

    public String extractText(String filePath) throws IOException {

        Path path = Path.of(filePath);

        try (PDDocument document = PDDocument.load(path.toFile())) {

            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            return pdfTextStripper.getText(document);
        }
    }
}