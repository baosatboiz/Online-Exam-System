package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.UpdateVocabularyItemCommand;
import com.example.toeicwebsite.application.result.VocabularyItemResult;

public interface UpdateVocabularyItem {
    VocabularyItemResult execute(UpdateVocabularyItemCommand command);
}