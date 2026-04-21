package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.DeleteScheduleCommand;
import com.example.toeicwebsite.application.usecase.DeleteSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteScheduleImpl implements DeleteSchedule {
    private final ExamScheduleRepository examScheduleRepository;
    @Override
    public void execute(DeleteScheduleCommand command) {
        ExamSchedule schedule = examScheduleRepository.findByBusinessId(command.examScheduleId().value())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch thi"));

        // Business Validation ( optional)

        examScheduleRepository.delete(schedule);
    }
}
