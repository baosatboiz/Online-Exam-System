package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface ExamAttemptEntityMapper {

    default ExamAttemptEntity toEntity(ExamAttempt domain,
                                       ExamScheduleEntity examScheduleEntity,
                                       String userId) {
        if (domain == null) return null;

        ExamAttemptEntity entity = new ExamAttemptEntity();

        entity.setBusinessId(domain.getId().value());
        entity.setUserId(userId);
        entity.setExamSchedule(examScheduleEntity);
        entity.setStartedAt(toLocal(domain.getStartedAt()));
        entity.setMustFinishedAt(toLocal(domain.getMustFinishedAt()));
        entity.setFinishedAt(toLocal(domain.getFinishedAt()));
        entity.setStatus(domain.getStatus());

        return entity;
    }

    private static LocalDateTime toLocal(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}

