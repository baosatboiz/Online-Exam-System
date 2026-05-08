package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;

public record GenerateVocabularyCommand(
    String word,
    String provider,
    UserId userId
) {
  
}
