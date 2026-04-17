package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import com.example.toeicwebsite.infrastucture.persistence.projection.ExamScheduleProjection;
import org.mapstruct.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExamScheduleMapper {
    @Mapping(target = "examScheduleId", source = "businessId")
    @Mapping(target = "examId", source = "exam")
    @Mapping(target = "partType", source = "partNumber")
    ExamSchedule toDomain(ExamScheduleEntity entity);

    @Mapping(target = "examScheduleId", source = "businessId")
    @Mapping(target = "examId", source = "examBusinessId")
    @Mapping(target = "partType", source = "partNumber")
    ExamSchedule toDomain(ExamScheduleProjection projection);

    default PartType map(Integer partNumber) {
        return PartType.fromCode(partNumber);
    }


    @Mapping(target = "businessId",source="examScheduleId")
    @Mapping(target = "partNumber", source = "partType")
    ExamScheduleEntity toEntity(ExamSchedule domain, @Context ExamEntity exam);

    default Integer map(PartType partType) {
        return partType == null ? null : partType.getCode();
    }

    default LocalDateTime map(Instant instant) {
        return instant == null ? null
                : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
    default UUID map(ExamScheduleId examScheduleId){ return examScheduleId.value();}
    default ExamScheduleId map(UUID businessId) {
        return new ExamScheduleId(businessId);
    }

    default ExamId map(ExamEntity examEntity) {
        if (examEntity == null) return null;
        UUID uuid = examEntity.getBusinessId();
        return new ExamId(uuid);
    }

    default ExamId mapExamBusinessId(UUID examBusinessId){
        return new ExamId(examBusinessId);
    }
    default Instant map(LocalDateTime time) {
        return time == null ? null : time.toInstant(ZoneOffset.UTC);
    }
    @AfterMapping
    static void fill(@MappingTarget ExamScheduleEntity examScheduleEntity,@Context ExamEntity exam){
        examScheduleEntity.setExam(exam);
    }
}
