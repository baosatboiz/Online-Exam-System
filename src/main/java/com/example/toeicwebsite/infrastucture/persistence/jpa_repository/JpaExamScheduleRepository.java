package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaExamScheduleRepository extends JpaRepository<ExamScheduleEntity, Long> {
    Optional<ExamScheduleEntity> findByBusinessId(UUID businessId);
    List<ExamScheduleEntity> findByMode(ExamMode mode, Pageable pageable);

    void deleteByBusinessId(UUID businessId);
}
