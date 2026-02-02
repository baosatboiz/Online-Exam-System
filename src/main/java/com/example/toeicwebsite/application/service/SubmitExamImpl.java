package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.SubmitExamCommand;
import com.example.toeicwebsite.application.result.SubmitExamResult;
import com.example.toeicwebsite.application.usecase.SubmitExam;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import com.example.toeicwebsite.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SubmitExamImpl implements SubmitExam {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;

    @Override
    public SubmitExamResult execute(SubmitExamCommand command) {
        ExamAttempt examAttempt = examAttemptRepository.findByBusinessId(command.examAttemptId().value())
                .orElseThrow(() -> new DomainException("Exam Attempt not found"));
        Instant now = Instant.now();
        examAttempt.finish(now);
        ExamSchedule examSchedule = examScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(() -> new DomainException("Exam Schedule not found"));
        Exam exam = examRepository.findFullExam(examSchedule.getExamId().value());
        examAttempt.calculateScore(exam);
        examAttemptRepository.update(examAttempt);
        return new SubmitExamResult(
                examAttempt.getScore().getListening(),
                examAttempt.getScore().getReading(),
                examAttempt.getScore().getTotalCorrect(),
                examAttempt.getStartedAt(),
                now
        );
    }
}
