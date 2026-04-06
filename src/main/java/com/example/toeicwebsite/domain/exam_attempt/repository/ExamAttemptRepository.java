package com.example.toeicwebsite.domain.exam_attempt.repository;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.question_bank.model.Question;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ExamAttemptRepository {
    ExamAttempt save(ExamAttempt examAttempt, UUID userId);
    ExamAttempt update(ExamAttempt examAttempt);
    Optional<ExamAttempt> findByBusinessId(UUID businessId);
    Optional<ExamAttempt> findByBusinessIdMinimal(UUID businessId);
    void saveAnsweredQuestion(ExamAttemptId attemptId, Long questionId, ChoiceKey choiceKey);
    Map<ExamScheduleId,Long> countTotalAttemptsIn(List<ExamScheduleId> ids);
    Map<ExamScheduleId, ExamStatus> findByUserIdAndScheduleIdsIn(UUID userId, List<ExamScheduleId> ids);
}
