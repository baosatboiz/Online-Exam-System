package com.example.toeicwebsite.web.mapper.schedule_mapper;

import com.example.toeicwebsite.application.command.CreateScheduleCommand;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.web.dto.create_schedule.CreateScheduleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CreateScheduleMapper {
    @Mapping(target = "partType", source = "partNumber")
    CreateScheduleCommand toCommand(CreateScheduleRequest request);
    default ExamId map(UUID id){
        return new ExamId(id);
    }
    default ExamMode map(String mode){
        return ExamMode.valueOf(mode);
    }
    default PartType map(Integer partNumber){
        return PartType.fromCode(partNumber);
    }
}
