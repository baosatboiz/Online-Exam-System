package com.example.toeicwebsite.application.query;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

public record GetVocabularyItemsBySetQuery(
        UserId userId,
        VocabularySetId setId
) {
}
