package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.port.ExamAttemptPendingAnswerPort;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.FailedAnswerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForceSubmitExpiredExamImpl {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptPendingAnswerPort pendingAnswerPort;
    private final FailedAnswerHandler failedAnswerHandler;

    @Transactional
    public void forceSubmit(String examAttemptIdValue) {
        ExamAttempt examAttempt = examAttemptRepository.findByBusinessId(UUID.fromString(examAttemptIdValue))
                .orElseThrow(() -> new DomainNotFoundException("Exam Attempt not found"));
        
        Instant now = Instant.now();
        examAttempt.expire(now);
        
        ExamSchedule examSchedule = examScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(() -> new DomainNotFoundException("Exam Schedule not found"));
        Exam exam = examRepository.findFullExam(examSchedule.getExamId().value());

        Map<Long, ChoiceKey> pendingAnswers = new HashMap<>();
        try {
            pendingAnswers = pendingAnswerPort.getAndClearPendingAnswers(examAttempt.getId());
        } catch (Exception e) {
        }

        Map<Long, ChoiceKey> dlqAnswers = failedAnswerHandler.getFailedAnswers(examAttempt.getId());
        dlqAnswers.forEach(pendingAnswers::putIfAbsent);
        if (!dlqAnswers.isEmpty()) {
            failedAnswerHandler.clearFailedAnswers(examAttempt.getId());
        }

        pendingAnswers.forEach((questionId, choiceKey) -> {
            examAttempt.restoreAnswer(questionId, choiceKey);
        });
        examAttemptRepository.saveAnsweredQuestions(examAttempt.getId(), pendingAnswers);

        examAttempt.calculateScore(exam);
        examAttemptRepository.update(examAttempt);
    }
}
