package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.GetPartQuestionQuery;
import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;

public interface GetPartQuestions {
    GetAttemptQuestionsResult handle(GetPartQuestionQuery query);
}
