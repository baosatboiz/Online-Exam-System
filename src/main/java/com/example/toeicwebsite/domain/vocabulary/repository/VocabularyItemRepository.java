package com.example.toeicwebsite.domain.vocabulary.repository;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItemId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VocabularyItemRepository {
    VocabularyItem save(VocabularyItem item);
    List<VocabularyItem> saveAll(List<VocabularyItem> items);
    List<VocabularyItem> findBySetIdAndUserId(VocabularySetId setId, UserId userId);
    Optional<VocabularyItem> findByIdAndUserId(VocabularyItemId itemId, UserId userId);
    long countBySetIdAndUserId(VocabularySetId setId, UserId userId);
    Map<VocabularySetId, Long> countBySetIdsAndUserId(List<VocabularySetId> setIds, UserId userId);
    void delete(VocabularyItemId itemId);
    void deleteAllByIds(List<VocabularyItemId> itemIds);
}
