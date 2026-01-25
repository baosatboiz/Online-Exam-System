package com.example.toeicwebsite.domain.exam_schedule.repository;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamScheduleRepository {
    Optional<ExamSchedule> findByBusinessId(UUID id);
}
