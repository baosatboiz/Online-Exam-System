package com.example.toeicwebsite.domain;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import java.util.Map;

public interface FailedAnswerHandler {

    void pushFailedAnswer(ExamAttemptId examAttemptId, Long questionId, ChoiceKey choiceKey);

    Map<Long, ChoiceKey> getFailedAnswers(ExamAttemptId examAttemptId);

    void clearFailedAnswers(ExamAttemptId examAttemptId);
}