package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;

import java.time.Instant;

public record CreateScheduleCommand(
        ExamId examId,
        Instant openAt,
        Instant endAt,
        ExamMode examMode
) {
}
