package com.offerbound.backend.repository;

import com.offerbound.backend.entity.Analysis;
import com.offerbound.backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findByResume(Resume resume);

    List<Analysis> findByResumeId(Long resumeId);

}