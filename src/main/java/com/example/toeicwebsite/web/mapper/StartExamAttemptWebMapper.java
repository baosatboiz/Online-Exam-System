package com.example.toeicwebsite.web.mapper;

import com.example.toeicwebsite.application.command.StartExamAttemptCommand;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.web.dto.request.StartExamAttemptRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StartExamAttemptWebMapper {

    @Mapping(target = "examScheduleId", source = "request.examScheduleId")
    @Mapping(target = "userId", expression = "java(userId)")
    StartExamAttemptCommand toCommand(
            StartExamAttemptRequest request,
            @Context String userId
    );

    default ExamScheduleId map(UUID id) {
        return new ExamScheduleId(id);
    }
}
