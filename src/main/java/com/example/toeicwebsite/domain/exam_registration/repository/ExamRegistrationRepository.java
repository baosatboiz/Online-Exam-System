package com.example.toeicwebsite.domain.exam_registration.repository;

import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;

import java.time.Instant;

public interface ExamRegistrationRepository {
    Long countByScheduleId(ExamScheduleId examScheduleId);
    boolean existsOverlapping(UserId userId, Instant openAt, Instant endAt);
    boolean existsByUserIdAndScheduleId(UserId userId,ExamScheduleId examScheduleId);
    java.util.Optional<ExamRegistration> findByUserIdAndScheduleId(UserId userId,ExamScheduleId examScheduleId);
    ExamRegistration save(ExamRegistration examRegistration);
    java.util.Optional<ExamRegistration> findById(com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId id);
}
