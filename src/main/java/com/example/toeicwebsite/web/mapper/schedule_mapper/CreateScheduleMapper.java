package com.example.toeicwebsite.web.mapper.schedule_mapper;

import com.example.toeicwebsite.application.command.CreateScheduleCommand;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.web.dto.create_schedule.CreateScheduleRequest;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CreateScheduleMapper {
    CreateScheduleCommand toCommand(CreateScheduleRequest request);
    default ExamId map(UUID id){
        return new ExamId(id);
    }
    default ExamMode map(String mode){
        return ExamMode.valueOf(mode);
    }

}
