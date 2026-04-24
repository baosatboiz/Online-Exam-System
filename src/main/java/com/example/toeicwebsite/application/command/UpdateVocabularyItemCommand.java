package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItemId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

public record UpdateVocabularyItemCommand(
        UserId userId,
        VocabularySetId setId,
        VocabularyItemId itemId,
        String term,
        String meaning,
        String note,
        String example
) {
}