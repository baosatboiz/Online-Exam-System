package com.example.toeicwebsite.web.mapper.schedule_mapper;

import com.example.toeicwebsite.application.query.GetScheduleQuery;
import com.example.toeicwebsite.application.result.GetScheduleResult;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleRequest;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GetScheduleMapper {
        @Mapping(target ="page",defaultValue = "0")
        @Mapping(target = "mode",defaultValue = "PRACTICE")
        GetScheduleQuery toQuery(GetScheduleRequest request,String userId);
        default ExamMode toExamMode(String mode){
            return ExamMode.valueOf(mode);
        }
        GetScheduleResponse toResponse(GetScheduleResult result);
        default UUID map(ExamScheduleId examScheduleId){
            return examScheduleId.value();
        }
        default String map(ExamStatus status){
            return status.name();
        }
        default String map(ExamMode examMode){return examMode.name();}
}
