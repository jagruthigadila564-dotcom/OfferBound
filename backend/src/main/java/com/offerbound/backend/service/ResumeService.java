package com.offerbound.backend.service;

import com.offerbound.backend.dto.ResumeResponse;
import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.entity.User;
import com.offerbound.backend.repository.ResumeRepository;
import com.offerbound.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    // Upload Resume
    public String uploadResume(Long userId,
                               MultipartFile file) throws IOException {

        // Check User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create uploads folder
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String fileName = System.currentTimeMillis()
                + "_"
                + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);

        // Save file
        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // Save Resume in DB
        Resume resume = new Resume();

        resume.setFileName(file.getOriginalFilename());
        resume.setFileType(file.getContentType());
        resume.setFilePath(filePath.toString());
        resume.setUser(user);

        resumeRepository.save(resume);

        return "Resume Uploaded Successfully";
    }

    // Get All Resumes of User
    public List<ResumeResponse> getUserResumes(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Resume> resumes = resumeRepository.findByUser(user);

        List<ResumeResponse> response = new ArrayList<>();

        for (Resume resume : resumes) {

            response.add(

                    new ResumeResponse(

                            resume.getId(),
                            resume.getFileName(),
                            resume.getFileType(),
                            resume.getUploadedAt()

                    )

            );
        }

        return response;
    }

    // Delete Resume
    public String deleteResume(Long resumeId) throws IOException {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found"));

        Files.deleteIfExists(Paths.get(resume.getFilePath()));

        resumeRepository.delete(resume);

        return "Resume Deleted Successfully";
    }

}