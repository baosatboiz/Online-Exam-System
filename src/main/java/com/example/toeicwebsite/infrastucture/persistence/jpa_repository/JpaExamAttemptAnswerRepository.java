package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaExamAttemptAnswerRepository extends JpaRepository<ExamAttemptAnswerEntity, Long> {
    Optional<ExamAttemptAnswerEntity> findByQuestionIdAndExamAttemptId(Long questionId, Long examAttemptId);
}
