package com.example.toeicwebsite.web.dto.ai;

import java.time.Instant;

public record AiConfigResponse(
        String provider,
        Instant createdAt,
        Instant updatedAt
) {
}
