package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;

import java.time.Instant;

public record GetScheduleResult(
        ExamScheduleId scheduleId,
        String title,
        int duration,
        Instant openAt,
        Instant closeAt,
        Long totalAttempts,
        ExamStatus userStatus

) {
}
