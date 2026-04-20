package com.example.toeicwebsite.web.dto.vocabulary.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateVocabularySetWithItemsRequest(
        @NotBlank(message = "Set name is required")
        String name,
        String description,
        @Valid
        List<ItemRequest> items
) {
    public record ItemRequest(
            String term,
            String meaning,
            String note,
            String example
    ) {
    }
}
