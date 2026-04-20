package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.query.GetVocabularyItemsBySetQuery;
import com.example.toeicwebsite.application.result.VocabularyItemResult;
import com.example.toeicwebsite.application.usecase.GetVocabularyItemsBySet;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetVocabularyItemsBySetImpl implements GetVocabularyItemsBySet {
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyItemRepository vocabularyItemRepository;

    @Override
    public List<VocabularyItemResult> handle(GetVocabularyItemsBySetQuery query) {
        vocabularySetRepository.findByIdAndUserId(query.setId(), query.userId())
                .orElseThrow(() -> new DomainNotFoundException("Vocabulary set not found"));

        List<VocabularyItem> items = vocabularyItemRepository.findBySetIdAndUserId(query.setId(), query.userId());
        return items.stream().map(item -> new VocabularyItemResult(
                item.getVocabularyItemId(),
                item.getTerm(),
                item.getMeaning(),
                item.getNote(),
                item.getExample(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        )).toList();
    }
}
