package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.UserProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserProviderRepository extends JpaRepository<UserProviderEntity, Long> {
    Optional<UserProviderEntity> findByProviderAndProviderId(String provider, String providerId);
}
