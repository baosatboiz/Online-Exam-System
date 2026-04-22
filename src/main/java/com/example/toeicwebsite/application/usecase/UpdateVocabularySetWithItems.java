package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.UpdateVocabularySetWithItemsCommand;
import com.example.toeicwebsite.application.result.VocabularySetResult;

public interface UpdateVocabularySetWithItems {
    VocabularySetResult execute(UpdateVocabularySetWithItemsCommand command);
}
