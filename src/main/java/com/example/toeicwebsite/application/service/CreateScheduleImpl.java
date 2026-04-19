package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.CreateScheduleCommand;
import com.example.toeicwebsite.application.result.CreateScheduleResult;
import com.example.toeicwebsite.application.usecase.CreateSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import com.example.toeicwebsite.domain.shared.Money;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateScheduleImpl implements CreateSchedule {
    private final ExamScheduleRepository examScheduleRepository;
    @Override
    public CreateScheduleResult execute(CreateScheduleCommand command) {
        ExamSchedule examSchedule;
        if (command.examMode() == ExamMode.PRACTICE) {
            examSchedule = ExamSchedule.createPractice(command.examId(), command.partType());
        } else {
            // Default maxSlot and price for REAL mode since it's not provided in CreateScheduleCommand initially
            examSchedule = ExamSchedule.createReal(
                    command.examId(), 
                    command.openAt(), 
                    command.endAt(), 
                    command.partType(), 
                    100, 
                    new Money(java.math.BigDecimal.valueOf(100000), "VND")
            );
        }
        ExamSchedule saved = examScheduleRepository.save(examSchedule);
        return new CreateScheduleResult(saved.getExamScheduleId());
    }
}
