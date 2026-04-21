package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;

public record CreateScheduleResult(
        ExamScheduleId examScheduleId
) {
}
