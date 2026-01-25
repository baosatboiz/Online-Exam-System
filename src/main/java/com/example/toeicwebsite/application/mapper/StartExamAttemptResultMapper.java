package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.StartExamAttemptResult;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import org.springframework.stereotype.Component;

@Component
public class StartExamAttemptResultMapper {

    public StartExamAttemptResult from(ExamAttempt attempt) {
        return new StartExamAttemptResult(
                attempt.getId(),
                attempt.getStartedAt(),
                attempt.getMustFinishedAt()
        );
    }
}

