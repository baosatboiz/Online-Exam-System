package com.example.toeicwebsite.web.dto.vocabulary.response;

import java.time.Instant;
import java.util.UUID;

public record VocabularyItemResponse(
        UUID itemId,
        String term,
        String meaning,
        String note,
        String example,
        String pronunciation,
        String audioUrl,
        Instant createdAt,
        Instant updatedAt
) {
}
