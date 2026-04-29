package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.UpdateVocabularyItemCommand;
import com.example.toeicwebsite.application.result.VocabularyItemResult;
import com.example.toeicwebsite.application.usecase.UpdateVocabularyItem;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import com.example.toeicwebsite.infrastucture.external.dictionary.DictionaryApiClient;
import com.example.toeicwebsite.infrastucture.external.dictionary.dto.PronunciationData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateVocabularyItemImpl implements UpdateVocabularyItem {
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyItemRepository vocabularyItemRepository;
    private final DictionaryApiClient dictionaryApiClient;

    @Override
    @Transactional
    public VocabularyItemResult execute(UpdateVocabularyItemCommand command) {
        vocabularySetRepository.findByIdAndUserId(command.setId(), command.userId())
                .orElseThrow(() -> new DomainNotFoundException("Vocabulary set not found"));

        if (command.term() == null || command.term().isBlank()) {
            throw new BusinessRuleException("Term cannot be empty");
        }
        if (command.meaning() == null || command.meaning().isBlank()) {
            throw new BusinessRuleException("Meaning cannot be empty");
        }

        VocabularyItem currentItem = vocabularyItemRepository.findByIdAndUserId(command.itemId(), command.userId())
                .orElseThrow(() -> new DomainNotFoundException("Vocabulary item not found"));

        if (!currentItem.getVocabularySetId().equals(command.setId())) {
            throw new DomainNotFoundException("Vocabulary item not found in this set");
        }

        PronunciationData pronunciationData = dictionaryApiClient.fetchPronunciation(command.term())
                .orElse(null);

        VocabularyItem updatedItem = currentItem.updateWithPronunciation(
                command.term(),
                command.meaning(),
                command.note(),
                command.example(),
                pronunciationData != null ? pronunciationData.pronunciation() : null,
                pronunciationData != null ? pronunciationData.audioUrl() : null
        );

        VocabularyItem saved = vocabularyItemRepository.save(updatedItem);

        return new VocabularyItemResult(
                saved.getVocabularyItemId(),
                saved.getTerm(),
                saved.getMeaning(),
                saved.getNote(),
                saved.getExample(),
                saved.getPronunciation(),
                saved.getAudioUrl(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}