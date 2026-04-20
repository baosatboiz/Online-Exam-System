package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.query.GetVocabularySetsQuery;
import com.example.toeicwebsite.application.result.VocabularySetResult;
import com.example.toeicwebsite.application.usecase.GetVocabularySets;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySet;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetVocabularySetsImpl implements GetVocabularySets {
    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyItemRepository vocabularyItemRepository;

    @Override
    public List<VocabularySetResult> handle(GetVocabularySetsQuery query) {
        List<VocabularySet> sets = vocabularySetRepository.findByUserId(query.userId());
        List<VocabularySetId> setIds = sets.stream().map(VocabularySet::getVocabularySetId).toList();
        Map<VocabularySetId, Long> itemCounts = vocabularyItemRepository.countBySetIdsAndUserId(setIds, query.userId());
        return sets.stream().map(set -> new VocabularySetResult(
                set.getVocabularySetId(),
                set.getName(),
                set.getDescription(),
                set.getCreatedAt(),
                set.getUpdatedAt(),
            itemCounts.getOrDefault(set.getVocabularySetId(), 0L)
        )).toList();
    }
}
