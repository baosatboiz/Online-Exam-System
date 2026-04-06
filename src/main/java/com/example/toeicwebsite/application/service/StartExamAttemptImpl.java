package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.StartExamAttemptCommand;
import com.example.toeicwebsite.application.mapper.StartExamAttemptResultMapper;
import com.example.toeicwebsite.application.result.StartExamAttemptResult;
import com.example.toeicwebsite.application.usecase.StartExamAttempt;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StartExamAttemptImpl implements StartExamAttempt {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final StartExamAttemptResultMapper startExamAttemptResultMapper;

    @Override
    public StartExamAttemptResult execute(StartExamAttemptCommand command) {
        Instant currentTime = Instant.now();
        ExamSchedule examSchedule = examScheduleRepository.findByBusinessId(command.examScheduleId().value())
                .orElseThrow(() -> new DomainNotFoundException("Exam schedule not found"));

        User user = userRepository.findByBusinessId(command.userId().value());

// ExamAttempt constructor includes this validation
//        if (!examSchedule.canStart(currentTime)) {
//            throw new DomainException("Exam schedule can't start now");
//        }

        UUID examBusinessId = examSchedule.getExamId().value();
//        Exam exam = examRepository.findByBusinessId(examBusinessId)
//                .orElseThrow(() -> new DomainException("Exam not found"));
        Integer duration = examRepository.findDurationByBusinessId(examBusinessId);

        ExamAttempt examAttempt = new ExamAttempt(ExamAttemptId.newId(), examSchedule, user, duration, currentTime);
        ExamAttempt saved = examAttemptRepository.save(examAttempt, command.userId().value());
        return startExamAttemptResultMapper.from(saved);
    }
}
