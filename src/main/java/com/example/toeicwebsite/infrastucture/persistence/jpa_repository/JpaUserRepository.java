package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity,Long>{
        Optional<UserEntity> findByEmail(String email);
        Optional<UserEntity> findByBussinessId(UUID id);
}
