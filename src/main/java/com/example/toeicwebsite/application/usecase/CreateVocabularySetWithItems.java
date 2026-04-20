package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.CreateVocabularySetWithItemsCommand;
import com.example.toeicwebsite.application.result.CreateVocabularySetWithItemsResult;

public interface CreateVocabularySetWithItems {
    CreateVocabularySetWithItemsResult execute(CreateVocabularySetWithItemsCommand command);
}
