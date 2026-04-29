package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItemId;

import java.time.Instant;

public record VocabularyItemResult(
        VocabularyItemId itemId,
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
