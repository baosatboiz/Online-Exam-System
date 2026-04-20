package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

import java.time.Instant;

public record VocabularySetResult(
        VocabularySetId setId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        long itemCount
) {
}
