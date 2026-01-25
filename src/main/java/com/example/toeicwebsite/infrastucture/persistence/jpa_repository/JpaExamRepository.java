package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaExamRepository extends JpaRepository<ExamEntity, Long> {
    Optional<ExamEntity> findByBusinessId(UUID businessId);
}
