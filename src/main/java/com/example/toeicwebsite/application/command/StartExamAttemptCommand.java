package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;

public record StartExamAttemptCommand(
        ExamScheduleId examScheduleId,
        UserId userId
) {}
