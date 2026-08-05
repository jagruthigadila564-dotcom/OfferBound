package com.offerbound.backend.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PdfParser {

    public String extractText(String filePath) throws IOException {

        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("Resume file not found.");
        }

        try (PDDocument document = PDDocument.load(file)) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);

        }

    }

}