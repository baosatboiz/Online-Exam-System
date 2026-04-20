package com.example.toeicwebsite.domain.vocabulary.repository;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

import java.util.List;
import java.util.Map;

public interface VocabularyItemRepository {
    VocabularyItem save(VocabularyItem item);
    List<VocabularyItem> saveAll(List<VocabularyItem> items);
    List<VocabularyItem> findBySetIdAndUserId(VocabularySetId setId, UserId userId);
    long countBySetIdAndUserId(VocabularySetId setId, UserId userId);
    Map<VocabularySetId, Long> countBySetIdsAndUserId(List<VocabularySetId> setIds, UserId userId);
}
