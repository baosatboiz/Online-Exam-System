package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaExamScheduleRepository extends JpaRepository<ExamScheduleEntity, Long> {
    Optional<ExamScheduleEntity> findByBusinessId(UUID businessId);
}
