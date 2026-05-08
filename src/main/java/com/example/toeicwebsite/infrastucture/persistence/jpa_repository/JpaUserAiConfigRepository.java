package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.UserAiConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserAiConfigRepository extends JpaRepository<UserAiConfigEntity, UUID> {
    Optional<UserAiConfigEntity> findByUserIdAndProvider(UUID userId, String provider);
    void deleteByUserIdAndProvider(UUID userId, String provider);
}
