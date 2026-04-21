package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;

import java.time.Instant;

public record GetScheduleResult(
        ExamScheduleId scheduleId,
        String title,
        int duration,
        Instant openAt,
        Instant closeAt,
        Long totalAttempts,
        ExamStatus userStatus,
        ExamMode examMode,
        PartType partType,
        boolean isRegistered
) {
}
