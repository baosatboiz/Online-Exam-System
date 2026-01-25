package com.example.toeicwebsite.domain.mapper;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import org.mapstruct.Mapper;

import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface ExamAttemptMapper {

    default ExamAttempt toDomain(ExamAttemptEntity entity) {
        if (entity == null) return null;

        return ExamAttempt.rehydrate(
                new ExamAttemptId(entity.getBusinessId()),
                new ExamScheduleId(entity.getExamSchedule().getBusinessId()),
                entity.getStartedAt().toInstant(ZoneOffset.UTC),
                entity.getMustFinishedAt().toInstant(ZoneOffset.UTC),
                entity.getFinishedAt() == null ? null : entity.getFinishedAt().toInstant(ZoneOffset.UTC),
                entity.getStatus()
        );
    }
}

