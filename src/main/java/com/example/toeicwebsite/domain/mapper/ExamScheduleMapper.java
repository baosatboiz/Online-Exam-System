package com.example.toeicwebsite.domain.mapper;

import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExamScheduleMapper {
    @Mapping(target = "examScheduleId", source = "businessId")
    @Mapping(target = "examId", source = "exam")
    @Mapping(target = "openAt", source = "openAt")
    @Mapping(target = "endAt", source = "endAt")
    @Mapping(target = "mode", source = "mode")
    ExamSchedule toDomain(ExamScheduleEntity entity);

    default ExamScheduleId map(UUID businessId) {
        return new ExamScheduleId(businessId);
    }

    default ExamId map(ExamEntity examEntity) {
        if (examEntity == null) return null;
        UUID uuid = UUID.nameUUIDFromBytes(
                ("Exam:" + examEntity.getId()).getBytes(StandardCharsets.UTF_8)
        );
        return new ExamId(uuid);
    }

    default Instant map(LocalDateTime time) {
        return time == null ? null : time.toInstant(ZoneOffset.UTC);
    }
}
