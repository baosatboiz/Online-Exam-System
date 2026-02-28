package com.example.toeicwebsite.domain.exam_schedule.repository;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExamScheduleRepository {
    Optional<ExamSchedule> findByBusinessId(UUID id);
    List<ExamSchedule> findBySpecification(int page, ExamMode mode);
}
