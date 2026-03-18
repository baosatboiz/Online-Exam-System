package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;

public record DeleteScheduleCommand(
        ExamScheduleId examScheduleId
) {
}
