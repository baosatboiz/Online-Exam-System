package com.example.toeicwebsite.domain.exam_registration.repository;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;

import java.time.Instant;

public interface ExamRegistrationRepository {
    Long countByScheduleId(ExamScheduleId examScheduleId);
    boolean existsOverlapping(UserId userId, Instant openAt, Instant endAt);
    boolean existsByUserIdAndScheduleId(UserId userId,ExamScheduleId examScheduleId);
}
