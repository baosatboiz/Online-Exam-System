package com.example.toeicwebsite.domain.exam.repository;

import com.example.toeicwebsite.domain.exam.model.Exam;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface ExamRepository {
    Exam save(Exam exam);
    Optional<Exam> findByBusinessId(UUID id);
    Integer findDurationByBusinessId(UUID id);
    Exam findFullExam(UUID id);
}
