package com.example.toeicwebsite.application.query;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;

public record GetScheduleQuery(
        int page,
        ExamMode mode,
        String userId
) {
}
