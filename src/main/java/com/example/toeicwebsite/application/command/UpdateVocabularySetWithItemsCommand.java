package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

import java.util.List;

public record UpdateVocabularySetWithItemsCommand(
        UserId userId,
        VocabularySetId setId,
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
