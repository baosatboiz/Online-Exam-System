package com.example.toeicwebsite.application.query;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;

public record GetPartQuestionQuery(
        ExamAttemptId examAttemptId,
        int partNumber
) {
}
