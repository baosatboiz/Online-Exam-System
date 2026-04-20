package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularyItemRepository;
import com.example.toeicwebsite.infrastucture.persistence.entity.VocabularyItemEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaVocabularyItemRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.VocabularyItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VocabularyItemRepositoryImpl implements VocabularyItemRepository {
    private final JpaVocabularyItemRepository jpaVocabularyItemRepository;
    private final VocabularyItemMapper vocabularyItemMapper;

    @Override
    public VocabularyItem save(VocabularyItem item) {
        VocabularyItemEntity entity = vocabularyItemMapper.toEntity(item);
        jpaVocabularyItemRepository.save(entity);
        return vocabularyItemMapper.toDomain(entity);
    }

    @Override
    public List<VocabularyItem> saveAll(List<VocabularyItem> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        return items.stream().map(item -> {
            VocabularyItemEntity entity = vocabularyItemMapper.toEntity(item);
            jpaVocabularyItemRepository.save(entity);
            return vocabularyItemMapper.toDomain(entity);
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
}
