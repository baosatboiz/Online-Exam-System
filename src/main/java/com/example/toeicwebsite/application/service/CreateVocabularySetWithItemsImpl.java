package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.CreateVocabularySetWithItemsCommand;
import com.example.toeicwebsite.application.result.CreateVocabularySetWithItemsResult;
import com.example.toeicwebsite.application.usecase.CreateVocabularySetWithItems;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySet;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import com.example.toeicwebsite.infrastucture.external.dictionary.DictionaryApiClient;
import com.example.toeicwebsite.infrastucture.external.dictionary.dto.PronunciationData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateVocabularySetWithItemsImpl implements CreateVocabularySetWithItems {
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyItemRepository vocabularyItemRepository;
    private final DictionaryApiClient dictionaryApiClient;

    @Override
    @Transactional
    public CreateVocabularySetWithItemsResult execute(CreateVocabularySetWithItemsCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("Set name cannot be empty");
        }

        VocabularySet set = vocabularySetRepository.save(VocabularySet.create(command.userId(), command.name(), command.description()));
        List<String> skipped = new ArrayList<>();
        Set<String> seenNormalized = new HashSet<>();
        List<VocabularyItem> validItems = new ArrayList<>();

        if (command.items() != null) {
            for (CreateVocabularySetWithItemsCommand.ItemInput input : command.items()) {
                if (input.term() == null || input.term().isBlank() || input.meaning() == null || input.meaning().isBlank()) {
                    continue;
                }

                String normalized = VocabularyItem.normalizeTerm(input.term());
                if (seenNormalized.contains(normalized)) {
                    skipped.add(input.term());
                    continue;
                }

                seenNormalized.add(normalized);

                PronunciationData pronunciationData = dictionaryApiClient.fetchPronunciation(input.term())
                        .orElse(null);

                validItems.add(VocabularyItem.createWithPronunciation(
                        set.getVocabularySetId(),
                        command.userId(),
                        input.term(),
                        input.meaning(),
                        input.note(),
                        input.example(),
                        pronunciationData != null ? pronunciationData.pronunciation() : null,
                        pronunciationData != null ? pronunciationData.audioUrl() : null
                ));
            }
        }

        int created = vocabularyItemRepository.saveAll(validItems).size();

        return new CreateVocabularySetWithItemsResult(set.getVocabularySetId(), created, skipped);
    }
}
