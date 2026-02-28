package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;

import java.time.Instant;

public record StartExamAttemptResult(
        ExamAttemptId attemptId,
        Instant startedAt,
        Instant mustFinishedAt
) {
}
