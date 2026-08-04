package com.offerbound.backend.repository;

import com.offerbound.backend.entity.Resume;
import com.offerbound.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    // Get all resumes uploaded by a specific user
    List<Resume> findByUser(User user);

}