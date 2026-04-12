package com.example.toeicwebsite.web.dto.get_exam_schedule;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;

import java.time.Instant;
import java.util.UUID;

public record GetScheduleResponse(
        UUID scheduleId,
        String title,
        int duration,
        Instant openAt,
        Instant closeAt,
        Long totalAttempts,
        String userStatus,
        String examMode,
        Integer partNumber
) {
}
