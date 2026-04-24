package com.example.toeicwebsite.web.dto.vocabulary.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateVocabularyItemRequest(
        @NotBlank(message = "Term is required")
        String term,
        @NotBlank(message = "Meaning is required")
        String meaning,
        String note,
        String example
) {
}