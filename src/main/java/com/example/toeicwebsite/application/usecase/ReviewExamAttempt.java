package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.ReviewExamAttemptQuery;
import com.example.toeicwebsite.application.result.ReviewExamAttemptResult;

public interface ReviewExamAttempt {
    ReviewExamAttemptResult handle(ReviewExamAttemptQuery query);
}
