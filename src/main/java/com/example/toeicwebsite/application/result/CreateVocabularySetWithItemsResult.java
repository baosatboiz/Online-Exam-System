package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

import java.util.List;

public record CreateVocabularySetWithItemsResult(
        VocabularySetId setId,
        int createdItemsCount,
        List<String> skippedTerms
) {
}
