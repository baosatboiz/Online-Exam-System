package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.shared.Money;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import com.example.toeicwebsite.infrastucture.persistence.projection.ExamScheduleProjection;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExamScheduleMapper {

    @Mapping(target = "examScheduleId", source = "businessId")
    @Mapping(target = "examId",         source = "exam")
    @Mapping(target = "partType",       source = "partNumber")
    @Mapping(target = "price",          source = "price")
    ExamSchedule toDomain(ExamScheduleEntity entity);

    @Mapping(target = "examScheduleId", source = "businessId")
    @Mapping(target = "examId",         source = "examBusinessId")
    @Mapping(target = "partType",       source = "partNumber")
    @Mapping(target = "price",          source = "price")
    ExamSchedule toDomain(ExamScheduleProjection projection);

    @Mapping(target = "businessId", source = "examScheduleId")
    @Mapping(target = "partNumber", source = "partType")
    @Mapping(target = "price",      source = "price")
    ExamScheduleEntity toEntity(ExamSchedule domain, @Context ExamEntity exam);


    default Money map(BigDecimal amount) {
        return amount != null ? new Money(amount, "VND") : null;
    }

    default BigDecimal map(Money money) {
        return money != null ? money.getAmount() : null;
    }

    default PartType map(Integer partNumber) {
        return PartType.fromCode(partNumber);
    }

    default Integer map(PartType partType) {
        return partType == null ? null : partType.getCode();
    }

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime time) {
        return time == null ? null : time.toInstant(ZoneOffset.UTC);
    }

    default UUID map(ExamScheduleId examScheduleId) {
        return examScheduleId.value();
    }

    default ExamScheduleId map(UUID businessId) {
        return new ExamScheduleId(businessId);
    }

    default ExamId map(ExamEntity examEntity) {
        if (examEntity == null) return null;
        return new ExamId(examEntity.getBusinessId());
    }

    default ExamId mapExamBusinessId(UUID examBusinessId) {
        return new ExamId(examBusinessId);
    }

    @AfterMapping
    static void fill(@MappingTarget ExamScheduleEntity entity, @Context ExamEntity exam) {
        entity.setExam(exam);
    }
}
