package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;

public record StartExamAttemptCommand(
        ExamScheduleId examScheduleId,
        String userId
) {}
