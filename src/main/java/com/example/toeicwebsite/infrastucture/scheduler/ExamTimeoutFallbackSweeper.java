package com.example.toeicwebsite.infrastucture.scheduler;

import com.example.toeicwebsite.application.service.ForceSubmitExpiredExamImpl;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExamTimeoutFallbackSweeper {

    private final ExamAttemptRepository examAttemptRepository;
    private final ForceSubmitExpiredExamImpl forceSubmitExpiredExam;

    @Scheduled(fixedDelay = 900000)
    public void sweepExpiredExams() {
        Instant cutoff = Instant.now().minus(5, ChronoUnit.MINUTES);
        List<ExamAttempt> expiredAttempts = examAttemptRepository.findExpiredAttempts(cutoff);

        if (expiredAttempts.isEmpty()) {
            return;
        }

        for (ExamAttempt attempt : expiredAttempts) {
            try {
                forceSubmitExpiredExam.forceSubmit(attempt.getId().value().toString());
            } catch (Exception e) {
            }
        }
    }
}
