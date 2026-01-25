package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaExamAttemptRepository extends JpaRepository<ExamAttemptEntity, Long> {
}
