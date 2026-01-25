package com.example.toeicwebsite.domain.exam_attempt.repository;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamAttemptRepository {
    ExamAttempt save(ExamAttempt examAttempt, String userId);
}
