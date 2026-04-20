package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.AddVocabularyItemCommand;
import com.example.toeicwebsite.application.result.VocabularyItemResult;
import com.example.toeicwebsite.application.usecase.AddVocabularyItem;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddVocabularyItemImpl implements AddVocabularyItem {
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyItemRepository vocabularyItemRepository;

    @Override
    @Transactional
    public VocabularyItemResult execute(AddVocabularyItemCommand command) {
        vocabularySetRepository.findByIdAndUserId(command.setId(), command.userId())
                .orElseThrow(() -> new DomainNotFoundException("Vocabulary set not found"));

        if (command.term() == null || command.term().isBlank()) {
            throw new BusinessRuleException("Term cannot be empty");
        }
        if (command.meaning() == null || command.meaning().isBlank()) {
            throw new BusinessRuleException("Meaning cannot be empty");
        }

        VocabularyItem item = VocabularyItem.create(
                command.setId(),
                command.userId(),
                command.term(),
                command.meaning(),
                command.note(),
                command.example()
        );
        VocabularyItem saved = vocabularyItemRepository.save(item);

        return new VocabularyItemResult(
                saved.getVocabularyItemId(),
                saved.getTerm(),
                saved.getMeaning(),
                saved.getNote(),
                saved.getExample(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
