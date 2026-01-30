package com.example.toeicwebsite.domain.exam_attempt.repository;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.question_bank.model.Question;

import java.util.Optional;
import java.util.UUID;

public interface ExamAttemptRepository {
    ExamAttempt save(ExamAttempt examAttempt, String userId);
    Optional<ExamAttempt> findByBusinessId(UUID businessId);
    void saveAnsweredQuestion(ExamAttempt attempt, Question question, ChoiceKey choiceKey);
}
