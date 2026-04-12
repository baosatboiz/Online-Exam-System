package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.CreateScheduleCommand;
import com.example.toeicwebsite.application.result.CreateScheduleResult;
import com.example.toeicwebsite.application.usecase.CreateSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateScheduleImpl implements CreateSchedule {
    private final ExamScheduleRepository examScheduleRepository;
    @Override
    public CreateScheduleResult execute(CreateScheduleCommand command) {
        ExamSchedule examSchedule = ExamSchedule.create(command.examId(),command.openAt(),command.endAt(),command.examMode(),command.partType());
        ExamSchedule saved = examScheduleRepository.save(examSchedule);
        return new CreateScheduleResult(saved.getExamScheduleId());
    }
}
