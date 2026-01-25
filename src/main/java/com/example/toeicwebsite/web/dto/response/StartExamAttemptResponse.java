package com.example.toeicwebsite.web.dto.response;

import java.time.Instant;

public record StartExamAttemptResponse(
        String attemptId,
        long remainingSeconds,
        Instant mustFinishedAt
) {}
