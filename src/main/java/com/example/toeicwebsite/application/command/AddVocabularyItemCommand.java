package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

public record AddVocabularyItemCommand(
        UserId userId,
        VocabularySetId setId,
        String term,
        String meaning,
        String note,
        String example
) {
}
