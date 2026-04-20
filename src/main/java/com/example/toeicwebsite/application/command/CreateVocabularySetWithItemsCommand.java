package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;

import java.util.List;

public record CreateVocabularySetWithItemsCommand(
        UserId userId,
        String name,
        String description,
        List<ItemInput> items
) {
    public record ItemInput(
            String term,
            String meaning,
            String note,
            String example
    ) {
    }
}
