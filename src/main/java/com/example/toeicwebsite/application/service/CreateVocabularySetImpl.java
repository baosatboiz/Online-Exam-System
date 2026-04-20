package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.CreateVocabularySetCommand;
import com.example.toeicwebsite.application.result.VocabularySetResult;
import com.example.toeicwebsite.application.usecase.CreateVocabularySet;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySet;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateVocabularySetImpl implements CreateVocabularySet {
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyItemRepository vocabularyItemRepository;

    @Override
    @Transactional
    public VocabularySetResult execute(CreateVocabularySetCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("Set name cannot be empty");
        }
        VocabularySet set = VocabularySet.create(command.userId(), command.name(), command.description());
        VocabularySet saved = vocabularySetRepository.save(set);
        long itemCount = vocabularyItemRepository.countBySetIdAndUserId(saved.getVocabularySetId(), saved.getUserId());
        return new VocabularySetResult(
                saved.getVocabularySetId(),
                saved.getName(),
                saved.getDescription(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                itemCount
        );
    }
}
