package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId;
import com.example.toeicwebsite.domain.exam_registration.repository.ExamRegistrationRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamRegistrationEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamRegistrationRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamRegistrationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExamRegistrationRepositoryImpl implements ExamRegistrationRepository {

    private final JpaExamRegistrationRepository jpaRepository;
    private final ExamRegistrationEntityMapper mapper;

    @Override
    public Long countByScheduleId(ExamScheduleId examScheduleId) {
        return jpaRepository.countByExamScheduleId(examScheduleId.value());
    }

    @Override
    public boolean existsOverlapping(UserId userId, Instant openAt, Instant endAt) {
        java.time.LocalDateTime openLocal = java.time.LocalDateTime.ofInstant(openAt, java.time.ZoneId.systemDefault());
        java.time.LocalDateTime endLocal = java.time.LocalDateTime.ofInstant(endAt, java.time.ZoneId.systemDefault());
        return jpaRepository.existsOverlappingSchedule(userId.value(), openLocal, endLocal);
    }

    @Override
    public boolean existsByUserIdAndScheduleId(UserId userId, ExamScheduleId examScheduleId) {
        return jpaRepository.existsByUserIdAndExamScheduleId(userId.value(), examScheduleId.value());
    }

    @Override
    public Optional<ExamRegistration> findByUserIdAndScheduleId(UserId userId, ExamScheduleId examScheduleId) {
        return jpaRepository.findByUserIdAndExamScheduleId(userId.value(), examScheduleId.value()).map(mapper::toDomain);
    }

    @Override
    public ExamRegistration save(ExamRegistration examRegistration) {
        ExamRegistrationEntity entity = mapper.toEntity(examRegistration);
        ExamRegistrationEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ExamRegistration> findById(ExamRegistrationId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }
}
