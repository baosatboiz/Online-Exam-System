package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExamAttemptMapper {
    @Mapping(source = "businessId", target = "id")
    @Mapping(source = "examSchedule", target = "examScheduleId")
    @Mapping(target = "answers", ignore = true)
    ExamAttempt toDomain(ExamAttemptEntity entity);

    default ExamAttemptId map(UUID businessId) {
        return new ExamAttemptId(businessId);
    }

    default ExamScheduleId map(ExamScheduleEntity entity) {
        return new ExamScheduleId(entity.getBusinessId());
    }

    default Instant map(LocalDateTime time) {
        return time == null ? null : time.toInstant(ZoneOffset.UTC);
    }
}

