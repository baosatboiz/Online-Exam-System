package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.VocabularySetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaVocabularySetRepository extends JpaRepository<VocabularySetEntity, UUID> {
    List<VocabularySetEntity> findByUserIdOrderByUpdatedAtDesc(UUID userId);
    Optional<VocabularySetEntity> findByIdAndUserId(UUID id, UUID userId);
}
