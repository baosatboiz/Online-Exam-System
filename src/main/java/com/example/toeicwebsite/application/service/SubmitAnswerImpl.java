package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.SubmitAnswerCommand;
import com.example.toeicwebsite.application.port.ExamAttemptPendingAnswerPort;
import com.example.toeicwebsite.application.result.SubmitAnswerResult;
import com.example.toeicwebsite.application.usecase.SubmitAnswer;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.FailedAnswerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class SubmitAnswerImpl implements SubmitAnswer {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamAttemptPendingAnswerPort pendingAnswerPort;
    private static final Logger log = LoggerFactory.getLogger(SubmitAnswerImpl.class);

    // Inject FailedAnswerHandler
    private final FailedAnswerHandler failedAnswerHandler;

    @Override
    public SubmitAnswerResult execute(SubmitAnswerCommand command) {
        Instant now = Instant.now();
        ExamAttempt attempt = examAttemptRepository.findByBusinessIdMinimal(command.examAttemptId().value())
                .orElseThrow(() -> new RuntimeException("Exam Attempt not found"));
        attempt.answer(command.questionId(), command.choiceKey(), now);

        // Asynchronous write to Redis with Circuit Breaker
        CompletableFuture.runAsync(() -> {
            try {
                pendingAnswerPort.savePendingAnswer(command.examAttemptId(), command.questionId(), command.choiceKey());
            } catch (Exception e) {
                log.error("Redis write failed: {}. Pushing to Dead Letter Queue.", e.getMessage());
                failedAnswerHandler.pushFailedAnswer(command.examAttemptId().value().toString(), command.questionId(), command.choiceKey());
            }
        });

        return new SubmitAnswerResult(command.questionId(), command.choiceKey());
    }
}
