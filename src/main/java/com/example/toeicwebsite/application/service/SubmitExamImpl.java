package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.SubmitExamCommand;
import com.example.toeicwebsite.application.port.ExamAttemptPendingAnswerPort;
import com.example.toeicwebsite.application.result.SubmitExamResult;
import com.example.toeicwebsite.application.usecase.SubmitExam;
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

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class SubmitExamImpl implements SubmitExam {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptPendingAnswerPort pendingAnswerPort;
    private final FailedAnswerHandler failedAnswerHandler;

    private static final Logger log = LoggerFactory.getLogger(SubmitExamImpl.class);

    @Override
    public SubmitExamResult execute(SubmitExamCommand command) {
        ExamAttempt examAttempt = examAttemptRepository.findByBusinessId(command.examAttemptId().value())
                .orElseThrow(() -> new DomainNotFoundException("Exam Attempt not found"));
        Instant now = Instant.now();
        examAttempt.finish(now);
        ExamSchedule examSchedule = examScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(() -> new DomainNotFoundException("Exam Schedule not found"));
        Exam exam = examRepository.findFullExam(examSchedule.getExamId().value());

        Map<Long, ChoiceKey> pendingAnswers = new HashMap<>();
        try {
            // Attempt to retrieve pending answers from Redis
            pendingAnswers = pendingAnswerPort.getAndClearPendingAnswers(command.examAttemptId());
        } catch (Exception e) {
            log.error("Redis failure: {}. Falling back to Dead Letter Queue.", e.getMessage());
        }

        // Process Dead Letter Queue and merge, then clear it
        String attemptIdStr = command.examAttemptId().value().toString();
        Map<Long, ChoiceKey> dlqAnswers = failedAnswerHandler.getFailedAnswers(attemptIdStr);
        dlqAnswers.forEach(pendingAnswers::putIfAbsent);
        if (!dlqAnswers.isEmpty()) {
            failedAnswerHandler.clearFailedAnswers(attemptIdStr);
        }

        // Merge answers from Redis and Dead Letter Queue into the attempt
        // We also save them synchronously to the DB to guarantee no data loss,
        // especially for DLQ answers which never reached the Stream Consumer.
        pendingAnswers.forEach((questionId, choiceKey) -> {
            examAttempt.restoreAnswer(questionId, choiceKey);
            examAttemptRepository.saveAnsweredQuestion(command.examAttemptId(), questionId, choiceKey);
        });

        examAttempt.calculateScore(exam);
        examAttemptRepository.update(examAttempt);
        return new SubmitExamResult(
                examAttempt.getScore().getListeningCorrect(),
                examAttempt.getScore().getReadingCorrect(),
                examAttempt.getScore().totalCorrect(),
                examAttempt.getScore().totalWrong(),
                examAttempt.getScore().totalUnanswered(),
                examAttempt.getStartedAt(),
                now
        );
    }
}
