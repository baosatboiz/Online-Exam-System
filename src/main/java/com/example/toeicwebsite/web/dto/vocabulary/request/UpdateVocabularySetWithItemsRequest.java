package com.example.toeicwebsite.web.dto.vocabulary.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateVocabularySetWithItemsRequest(
        @NotBlank(message = "Set name cannot be empty")
        String name,
        String description,
        List<ItemUpdate> items
) {
    public record ItemUpdate(
            String itemId,
            String term,
            String meaning,
            String note,
            String example,
            boolean isNew
    ) {
    }
}
