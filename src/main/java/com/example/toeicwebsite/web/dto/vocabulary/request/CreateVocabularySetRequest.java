package com.example.toeicwebsite.web.dto.vocabulary.request;

import jakarta.validation.constraints.NotBlank;

public record CreateVocabularySetRequest(
        @NotBlank(message = "Set name is required")
        String name,
        String description
) {
}
