package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.DeleteVocabularySetCommand;

public interface DeleteVocabularySet {
    void execute(DeleteVocabularySetCommand command);
}
