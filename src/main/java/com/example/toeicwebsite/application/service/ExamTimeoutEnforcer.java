package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamTimeoutEnforcer {

    private final ExamAttemptRepository examAttemptRepository;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void enforceExamTimeouts() {
        Instant now = Instant.now();
        List<ExamAttempt> inProgressAttempts = examAttemptRepository.findInProgressAttempts();

        for (ExamAttempt attempt : inProgressAttempts) {
            if (now.isAfter(attempt.getMustFinishedAt())) {
                try {
                    attempt.expire(now);
                    examAttemptRepository.update(attempt);
                    log.info("Enforced timeout for exam attempt: {}", attempt.getId());
                } catch (Exception e) {
                    log.error("Failed to enforce timeout for exam attempt: {}. Error: {}", attempt.getId(), e.getMessage());
                }
            }
        }
    }
}