package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.user.model.UserId;

import java.time.Instant;

public record GetAiConfigResult(
        String id,
        UserId userId,
        String provider,
        Instant createdAt,
        Instant updatedAt
) {
}
