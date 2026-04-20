package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.CreateVocabularySetCommand;
import com.example.toeicwebsite.application.result.VocabularySetResult;

public interface CreateVocabularySet {
    VocabularySetResult execute(CreateVocabularySetCommand command);
}
