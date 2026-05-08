package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.GenerateVocabularyCommand;
import com.example.toeicwebsite.infrastucture.external.ai.dto.AiVocabularyResponse;

public interface GenerateVocabulary {
    AiVocabularyResponse execute(GenerateVocabularyCommand command);
}
