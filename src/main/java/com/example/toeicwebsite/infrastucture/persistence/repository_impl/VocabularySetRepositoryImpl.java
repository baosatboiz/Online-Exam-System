package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySet;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;
import com.example.toeicwebsite.domain.vocabulary.repository.VocabularySetRepository;
import com.example.toeicwebsite.infrastucture.persistence.entity.VocabularySetEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaVocabularySetRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.VocabularySetMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VocabularySetRepositoryImpl implements VocabularySetRepository {
    private final JpaVocabularySetRepository jpaVocabularySetRepository;
    private final VocabularySetMapper vocabularySetMapper;
    private final EntityManager entityManager;

    @Override
    public VocabularySet save(VocabularySet set) {
        VocabularySetEntity entity = vocabularySetMapper.toEntity(set);
        VocabularySetEntity managed = isNew(set) ? persist(entity) : entityManager.merge(entity);
        return vocabularySetMapper.toDomain(managed);
    }

    @Override
    public List<VocabularySet> findByUserId(UserId userId) {
        return jpaVocabularySetRepository.findByUserIdOrderByUpdatedAtDesc(userId.value())
                .stream()
                .map(vocabularySetMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<VocabularySet> findByIdAndUserId(VocabularySetId setId, UserId userId) {
        return jpaVocabularySetRepository.findByIdAndUserId(setId.value(), userId.value()).map(vocabularySetMapper::toDomain);
    }

    @Override
    public void delete(VocabularySetId setId) {
        jpaVocabularySetRepository.deleteById(setId.value());
    }

    private boolean isNew(VocabularySet set) {
        return set.getCreatedAt() == null;
    }

    private VocabularySetEntity persist(VocabularySetEntity entity) {
        entityManager.persist(entity);
        return entity;
    }
}
