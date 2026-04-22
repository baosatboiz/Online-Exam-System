package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItemId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.infrastucture.persistence.entity.VocabularyItemEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaVocabularyItemRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.VocabularyItemMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VocabularyItemRepositoryImpl implements VocabularyItemRepository {
    private final JpaVocabularyItemRepository jpaVocabularyItemRepository;
    private final VocabularyItemMapper vocabularyItemMapper;
    private final EntityManager entityManager;

    @Override
    public VocabularyItem save(VocabularyItem item) {
        VocabularyItemEntity entity = vocabularyItemMapper.toEntity(item);
        VocabularyItemEntity managed = isNew(item) ? persist(entity) : entityManager.merge(entity);
        return vocabularyItemMapper.toDomain(managed);
    }

    @Override
    public List<VocabularyItem> saveAll(List<VocabularyItem> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        return items.stream().map(item -> {
            VocabularyItemEntity entity = vocabularyItemMapper.toEntity(item);
            VocabularyItemEntity managed = isNew(item) ? persist(entity) : entityManager.merge(entity);
            return vocabularyItemMapper.toDomain(managed);
        }).toList();
    }

    @Override
    public List<VocabularyItem> findBySetIdAndUserId(VocabularySetId setId, UserId userId) {
        return jpaVocabularyItemRepository.findBySetIdAndUserIdOrderByCreatedAtDesc(setId.value(), userId.value())
                .stream()
                .map(vocabularyItemMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<VocabularyItem> findByIdAndUserId(VocabularyItemId itemId, UserId userId) {
        return jpaVocabularyItemRepository.findByIdAndUserId(itemId.value(), userId.value())
                .map(vocabularyItemMapper::toDomain);
    }

    @Override
    public long countBySetIdAndUserId(VocabularySetId setId, UserId userId) {
        return jpaVocabularyItemRepository.countBySetIdAndUserId(setId.value(), userId.value());
    }

    @Override
    public Map<VocabularySetId, Long> countBySetIdsAndUserId(List<VocabularySetId> setIds, UserId userId) {
        if (setIds == null || setIds.isEmpty()) {
            return Map.of();
        }
        List<UUID> ids = setIds.stream().map(VocabularySetId::value).toList();
        return jpaVocabularyItemRepository.countBySetIdsAndUserId(ids, userId.value())
                .stream()
                .collect(Collectors.toMap(
                        row -> new VocabularySetId(row.getSetId()),
                        JpaVocabularyItemRepository.SetCountProjection::getItemCount
                ));
    }

    @Override
    public void delete(VocabularyItemId itemId) {
        jpaVocabularyItemRepository.deleteById(itemId.value());
    }

    @Override
    public void deleteAllByIds(List<VocabularyItemId> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return;
        }
        List<UUID> ids = itemIds.stream().map(VocabularyItemId::value).toList();
        jpaVocabularyItemRepository.deleteAllByIdInBatch(ids);
    }

    private boolean isNew(VocabularyItem item) {
        return item.getCreatedAt() == null;
    }

    private VocabularyItemEntity persist(VocabularyItemEntity entity) {
        entityManager.persist(entity);
        return entity;
    }
}
