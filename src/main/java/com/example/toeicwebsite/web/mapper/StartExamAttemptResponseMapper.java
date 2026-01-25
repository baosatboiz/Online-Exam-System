package com.example.toeicwebsite.web.mapper;

import com.example.toeicwebsite.application.result.StartExamAttemptResult;
import com.example.toeicwebsite.web.dto.response.StartExamAttemptResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class StartExamAttemptResponseMapper {

    public static StartExamAttemptResponse from(StartExamAttemptResult result) {
        long remainingSeconds = Duration.between(Instant.now(), result.mustFinishedAt()).getSeconds();

        return new StartExamAttemptResponse(
                result.attemptId().value().toString(),
                remainingSeconds,
                result.mustFinishedAt()
        );
    }
}

