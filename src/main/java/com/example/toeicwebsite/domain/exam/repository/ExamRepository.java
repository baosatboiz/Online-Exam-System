package com.example.toeicwebsite.domain.exam.repository;

import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ExamRepository {
    Exam save(Exam exam);
    Optional<Exam> findByBusinessId(UUID id);
    Integer findDurationByBusinessId(UUID id);
    Exam findFullExam(UUID id);
    Map<ExamId,Exam> findByBusinessIdIn(List<ExamId> ids);
}
