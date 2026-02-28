package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.GetAttemptQuestionsQuery;
import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;

public interface GetAttemptQuestions {
    GetAttemptQuestionsResult handle(GetAttemptQuestionsQuery command);
}
