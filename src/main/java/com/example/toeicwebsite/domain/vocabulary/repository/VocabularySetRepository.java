package com.example.toeicwebsite.domain.vocabulary.repository;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySet;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;

import java.util.List;
import java.util.Optional;

public interface VocabularySetRepository {
    VocabularySet save(VocabularySet set);
    List<VocabularySet> findByUserId(UserId userId);
    Optional<VocabularySet> findByIdAndUserId(VocabularySetId setId, UserId userId);
    void delete(VocabularySetId setId);
}
