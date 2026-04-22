package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

public record DeleteVocabularySetCommand(
        UserId userId,
        VocabularySetId setId
) {
}
