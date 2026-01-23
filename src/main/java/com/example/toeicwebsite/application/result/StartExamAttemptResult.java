package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;

public record StartExamAttemptResult(
        ExamAttemptId attemptId,
        int duration,
        ExamStatus status
) {
}
