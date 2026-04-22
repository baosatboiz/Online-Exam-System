package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.DeleteVocabularySetCommand;
import com.example.toeicwebsite.application.usecase.DeleteVocabularySet;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteVocabularySetImpl implements DeleteVocabularySet {
    private final VocabularySetRepository vocabularySetRepository;

    @Override
    @Transactional
    public void execute(DeleteVocabularySetCommand command) {
        vocabularySetRepository.findByIdAndUserId(command.setId(), command.userId())
                .orElseThrow(() -> new BusinessRuleException("Vocabulary set not found"));

        vocabularySetRepository.delete(command.setId());
    }
}
