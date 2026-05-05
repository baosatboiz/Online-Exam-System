package com.example.toeicwebsite.application.port;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

import java.util.Map;

public interface ExamAttemptPendingAnswerPort {
    void savePendingAnswer(ExamAttemptId examAttemptId, Long questionId, ChoiceKey choiceKey);
    Map<Long, ChoiceKey> getAndClearPendingAnswers(ExamAttemptId examAttemptId);
}
