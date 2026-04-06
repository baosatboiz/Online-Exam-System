package com.example.toeicwebsite.application.query;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.user.model.UserId;

public record GetScheduleQuery(
        int page,
        ExamMode mode,
        UserId userId
) {
}
