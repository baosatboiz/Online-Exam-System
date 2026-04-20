package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;

public record CreateVocabularySetCommand(
        UserId userId,
        String name,
        String description
) {
}
