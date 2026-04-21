package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExamAttemptMinimalMapper {
    @Mapping(source = "businessId", target = "id")
    @Mapping(target = "examScheduleId", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "score", ignore = true)
    ExamAttempt toDomainMinimal(ExamAttemptEntity entity);

    default ExamAttemptId map(UUID businessId) {
        return new ExamAttemptId(businessId);
    }

    default Instant map(LocalDateTime time) {
        return time == null ? null : time.toInstant(ZoneOffset.UTC);
    }
}
