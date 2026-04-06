package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.UserEntity;
import org.mapstruct.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExamAttemptEntityMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "businessId", source = "domain.id"),
            @Mapping(target = "examSchedule", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "startedAt", source = "domain.startedAt"),
            @Mapping(target = "mustFinishedAt", source = "domain.mustFinishedAt"),
            @Mapping(target = "finishedAt", source = "domain.finishedAt"),
            @Mapping(target = "answers", ignore = true),
            @Mapping(target = "status", source = "domain.status")
    })
    ExamAttemptEntity toEntity(ExamAttempt domain,
                               @Context ExamScheduleEntity examScheduleEntity,
                               @Context UserEntity userEntity);

    default UUID map(ExamAttemptId examAttemptId) {
        return examAttemptId.value();
    }
    @AfterMapping
    default void fillExtra(
            @MappingTarget ExamAttemptEntity entity,
            @Context ExamScheduleEntity examScheduleEntity,
            @Context UserEntity userEntity
    ) {
        entity.setExamSchedule(examScheduleEntity);
        entity.setUser(userEntity);
    }

    default LocalDateTime map(Instant instant) {
        return instant == null ? null
                : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @AfterMapping
    default void mapScoreToEntity(ExamAttempt domain,
                                  @MappingTarget ExamAttemptEntity entity) {
        if (domain.getScore() != null) {
            entity.setListeningScore(domain.getScore().getListeningCorrect());
            entity.setReadingScore(domain.getScore().getReadingCorrect());
        }
    }
}


