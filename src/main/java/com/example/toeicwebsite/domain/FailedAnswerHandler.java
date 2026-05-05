package com.example.toeicwebsite.domain;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import java.util.Map;

public interface FailedAnswerHandler {

    void pushFailedAnswer(String examAttemptId, Long questionId, ChoiceKey choiceKey);

    Map<Long, ChoiceKey> getFailedAnswers(String examAttemptId);

    void clearFailedAnswers(String examAttemptId);
}