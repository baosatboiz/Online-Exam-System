package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.assembler.ScheduleAssembler;
import com.example.toeicwebsite.application.query.GetScheduleQuery;
import com.example.toeicwebsite.application.result.GetScheduleResult;
import com.example.toeicwebsite.application.usecase.GetSchedule;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.repository.ExamRegistrationRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetScheduleImpl implements GetSchedule {
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final ScheduleAssembler scheduleAssembler;
    private final ExamRegistrationRepository examRegistrationRepository;
    @Override
    public List<GetScheduleResult> handle(GetScheduleQuery query) {
        List<ExamSchedule> schedules = examScheduleRepository.findBySpecification(query.page(), query.mode(), query.partType());
        List<ExamId> examIds = schedules.stream().map(ExamSchedule::getExamId).toList();
        List<ExamScheduleId> scheduleIds = schedules.stream().map(ExamSchedule::getExamScheduleId).toList();
        Map<ExamId,Exam> exams = examRepository.findByBusinessIdIn(examIds);
        Map<ExamScheduleId,Long> totalAttempts = examAttemptRepository.countTotalAttemptsIn(scheduleIds);
        Map<ExamScheduleId, ExamStatus> userAttemptStatus = examAttemptRepository.findByUserIdAndScheduleIdsIn(query.userId().value(),scheduleIds);
        
        List<ExamRegistration> registrations =
            examRegistrationRepository.findByUserIdAndScheduleIdIn(query.userId(), scheduleIds);
            
        Map<ExamScheduleId, Boolean> userRegistrationStatus = schedules.stream()
            .collect(Collectors.toMap(
                ExamSchedule::getExamScheduleId,
                schedule -> registrations.stream().anyMatch(r -> r.getExamScheduleId().equals(schedule.getExamScheduleId()) && r.isConfirmed())
            ));

        return scheduleAssembler.toResultList(schedules, exams, totalAttempts, userAttemptStatus, userRegistrationStatus);
    }
}
