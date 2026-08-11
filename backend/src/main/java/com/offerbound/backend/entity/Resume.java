package com.offerbound.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column
    private String fileType;

    @Column
    private String filePath;

    private LocalDateTime uploadedAt;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String resumeText;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Resume() {
    }

    public Resume(String fileName, String fileType, String filePath, String resumeText, User user, LocalDateTime uploadedAt) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.resumeText = resumeText;
        this.user = user;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}