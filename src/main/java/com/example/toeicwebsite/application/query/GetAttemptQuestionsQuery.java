package com.example.toeicwebsite.application.query;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.user.model.UserId;

public record GetAttemptQuestionsQuery(
        ExamAttemptId examAttemptId,
        UserId userId
) {}