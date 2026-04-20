package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.AddVocabularyItemCommand;
import com.example.toeicwebsite.application.result.VocabularyItemResult;

public interface AddVocabularyItem {
    VocabularyItemResult execute(AddVocabularyItemCommand command);
}
