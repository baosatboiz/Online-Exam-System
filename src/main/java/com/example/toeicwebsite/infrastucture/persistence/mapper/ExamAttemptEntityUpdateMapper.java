package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import org.mapstruct.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface ExamAttemptEntityUpdateMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "businessId", ignore = true),
            @Mapping(target = "examSchedule", ignore = true),
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "answers", ignore = true),

            @Mapping(target = "startedAt", source = "domain.startedAt"),
            @Mapping(target = "mustFinishedAt", source = "domain.mustFinishedAt"),
            @Mapping(target = "finishedAt", source = "domain.finishedAt"),
            @Mapping(target = "status", source = "domain.status")
    })
    void updateEntity(ExamAttempt domain,
                      @MappingTarget ExamAttemptEntity entity);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null
                : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @AfterMapping
    default void mapScoreToEntity(ExamAttempt domain,
                                  @MappingTarget ExamAttemptEntity entity) {
        if (domain.getScore() != null) {
            entity.setListeningScore(domain.getScore().getListening());
            entity.setReadingScore(domain.getScore().getReading());
        }
    }
}
