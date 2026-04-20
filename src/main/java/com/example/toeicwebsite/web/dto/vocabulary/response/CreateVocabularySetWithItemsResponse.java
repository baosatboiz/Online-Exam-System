package com.example.toeicwebsite.web.dto.vocabulary.response;

import java.util.List;
import java.util.UUID;

public record CreateVocabularySetWithItemsResponse(
        UUID setId,
        int createdItemsCount,
        List<String> skippedTerms
) {
}
