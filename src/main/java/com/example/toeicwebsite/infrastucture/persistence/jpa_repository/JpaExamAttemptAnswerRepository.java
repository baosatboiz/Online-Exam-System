package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaExamAttemptAnswerRepository extends JpaRepository<ExamAttemptAnswerEntity, Long> {
    Optional<ExamAttemptAnswerEntity> findByQuestionIdAndExamAttemptId(Long questionId, Long examAttemptId);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO exam_attempt_answer (attempt_id, question_id, choice_key)
        VALUES (:attemptId, :questionId, :choiceKey)
        ON CONFLICT (attempt_id, question_id)
        DO UPDATE SET choice_key = EXCLUDED.choice_key
    """, nativeQuery = true)
    void upsertAnswer(Long attemptId, Long questionId, String choiceKey);
}
