package com.example.toeicwebsite.web.dto.vocabulary.response;

import java.time.Instant;
import java.util.UUID;

public record VocabularySetResponse(
        UUID setId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        long itemCount
) {
}
