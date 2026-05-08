package com.example.toeicwebsite.application.result;

import java.time.Instant;

public record CreateAiConfigResult(
        String provider,
        Instant createdAt,
        Instant updatedAt
) {
}
