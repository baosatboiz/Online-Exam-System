package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId;
import com.example.toeicwebsite.domain.exam_registration.model.RegistrationStatus;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExamRegistrationEntityMapper {

    @Mapping(target = "id",                  ignore = true)
    @Mapping(target = "businessId",          source = "examRegistrationId")
    @Mapping(target = "examScheduleId",      source = "examScheduleId")
    @Mapping(target = "userId",              source = "userId")
    @Mapping(target = "registrationStatus",  source = "registrationStatus")
    ExamRegistrationEntity toEntity(ExamRegistration domain);

    @Mapping(target = "examRegistrationId",  source = "businessId")
    @Mapping(target = "examScheduleId",      source = "examScheduleId")
    @Mapping(target = "userId",              source = "userId")
    @Mapping(target = "registrationStatus",  source = "registrationStatus")
    ExamRegistration toDomain(ExamRegistrationEntity entity);

    // ── Type conversion helpers ──────────────────────────────────────────────

    default UUID map(ExamRegistrationId id) { return id != null ? id.value() : null; }
    default ExamRegistrationId mapToRegistrationId(UUID uuid) { return uuid != null ? new ExamRegistrationId(uuid) : null; }

    default UUID map(ExamScheduleId id) { return id != null ? id.value() : null; }
    default ExamScheduleId mapToScheduleId(UUID uuid) { return uuid != null ? new ExamScheduleId(uuid) : null; }

    default UUID map(UserId id) { return id != null ? id.value() : null; }
    default UserId mapToUserId(UUID uuid) { return uuid != null ? new UserId(uuid) : null; }

    default String map(RegistrationStatus status) { return status != null ? status.name() : null; }
    default RegistrationStatus mapToStatus(String status) { return status != null ? RegistrationStatus.valueOf(status) : null; }
}
