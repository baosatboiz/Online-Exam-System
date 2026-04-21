package com.example.toeicwebsite.application.query;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.user.model.UserId;

public record GetScheduleQuery(
        Integer page,
        ExamMode mode,
        UserId userId,
        PartType partType
) {
}
