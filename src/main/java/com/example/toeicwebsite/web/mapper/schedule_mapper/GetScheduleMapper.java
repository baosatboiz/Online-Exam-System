package com.example.toeicwebsite.web.mapper.schedule_mapper;

import com.example.toeicwebsite.application.query.GetScheduleQuery;
import com.example.toeicwebsite.application.result.GetScheduleResult;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleRequest;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GetScheduleMapper {
        @Mapping(target = "partType",source = "request.partNumber")
        GetScheduleQuery toQuery(GetScheduleRequest request, UserId userId);
        default ExamMode toExamMode(String mode){
            return mode==null?null:ExamMode.valueOf(mode);
        }
        default PartType toPartType(Integer partNumber){
            return PartType.fromCode(partNumber);
        }
        @Mapping(target = "partNumber",source = "partType")
        GetScheduleResponse toResponse(GetScheduleResult result);
        default UUID map(ExamScheduleId examScheduleId){
            return examScheduleId.value();
        }
        default String map(ExamStatus status){
            return status.name();
        }
        default String map(ExamMode examMode){return examMode.name();}
        default Integer map(PartType partType){return partType == null ? null : partType.getCode();}
}
