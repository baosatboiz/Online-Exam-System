package com.example.toeicwebsite.application.port;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;

public interface ExamTimeoutSchedulerPort {
    void scheduleTimeout(ExamAttemptId attemptId, int delayInMinutes);
}
