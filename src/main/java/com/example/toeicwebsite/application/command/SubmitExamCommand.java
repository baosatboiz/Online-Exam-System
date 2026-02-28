package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;

public record SubmitExamCommand(
        ExamAttemptId examAttemptId
) {
}
