package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.UpdateVocabularySetWithItemsCommand;
import com.example.toeicwebsite.application.result.VocabularySetResult;
import com.example.toeicwebsite.application.usecase.UpdateVocabularySetWithItems;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItemId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySet;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import com.example.toeicwebsite.infrastucture.external.dictionary.DictionaryApiClient;
import com.example.toeicwebsite.infrastucture.external.dictionary.dto.PronunciationData;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UpdateVocabularySetWithItemsImpl implements UpdateVocabularySetWithItems {
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyItemRepository vocabularyItemRepository;
    private final DictionaryApiClient dictionaryApiClient;

    @Override
    @Transactional
    public VocabularySetResult execute(UpdateVocabularySetWithItemsCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("Set name cannot be empty");
        }

        VocabularySet set = vocabularySetRepository.findByIdAndUserId(command.setId(), command.userId())
                .orElseThrow(() -> new BusinessRuleException("Vocabulary set not found"));

        List<VocabularyItem> currentItems = vocabularyItemRepository.findBySetIdAndUserId(command.setId(), command.userId());
        Map<String, VocabularyItem> currentItemsById = currentItems.stream()
                                                        .collect(Collectors.toMap(
                                                            item -> item.getVocabularyItemId().value().toString(),
                                                            item -> item
                                                        ));

        List<VocabularyItem> items = new ArrayList<>();
        Set<String> incomingItemIds = new HashSet<>();

        if (command.items() != null && !command.items().isEmpty()) {
            for (UpdateVocabularySetWithItemsCommand.ItemUpdate itemUpdate : command.items()) {
                if (itemUpdate.term() == null || itemUpdate.term().isBlank() || 
                    itemUpdate.meaning() == null || itemUpdate.meaning().isBlank()) {
                    continue;
                }

                PronunciationData pronunciationData = dictionaryApiClient.fetchPronunciation(itemUpdate.term())
                    .orElse(null);

                if (itemUpdate.isNew()) {
                    VocabularyItem newItem = VocabularyItem.createWithPronunciation(
                            command.setId(),
                            command.userId(),
                            itemUpdate.term(),
                            itemUpdate.meaning(),
                            itemUpdate.note(),
                            itemUpdate.example(),
                            pronunciationData != null ? pronunciationData.pronunciation() : null,
                            pronunciationData != null ? pronunciationData.audioUrl() : null
                    );
                    items.add(newItem);
                    incomingItemIds.add(newItem.getVocabularyItemId().value().toString());
                } else if (itemUpdate.itemId() != null && !itemUpdate.itemId().isBlank()) {
                    VocabularyItem existingItem = currentItemsById.get(itemUpdate.itemId());
                    if (existingItem == null) {
                        throw new BusinessRuleException("Item not found");
                    }
                    
                    VocabularyItem updatedItem = existingItem.updateWithPronunciation(
                            itemUpdate.term(),
                            itemUpdate.meaning(),
                            itemUpdate.note(),
                            itemUpdate.example(),
                            pronunciationData != null ? pronunciationData.pronunciation() : null,
                            pronunciationData != null ? pronunciationData.audioUrl() : null
                    );
                    items.add(updatedItem);
                    incomingItemIds.add(itemUpdate.itemId());
                }
            }
        }

        // Save new and updated items
        List<VocabularyItem> savedItems = items;
        if (!items.isEmpty()) {
            savedItems = vocabularyItemRepository.saveAll(items);
        }

        // Delete items that were removed
        List<VocabularyItemId> removedItemIds = currentItems.stream()
                .map(VocabularyItem::getVocabularyItemId)
                .filter(itemId -> !incomingItemIds.contains(itemId.value().toString()))
                .toList();
        vocabularyItemRepository.deleteAllByIds(removedItemIds);

        VocabularySet updatedSet = set.updateWithItems(command.name(), command.description(), savedItems);
        VocabularySet savedSet = vocabularySetRepository.save(updatedSet);

        return new VocabularySetResult(
                savedSet.getVocabularySetId(),
                savedSet.getName(),
                savedSet.getDescription(),
                savedSet.getCreatedAt(),
                savedSet.getUpdatedAt(),
                savedItems.size()
        );
    }
}
