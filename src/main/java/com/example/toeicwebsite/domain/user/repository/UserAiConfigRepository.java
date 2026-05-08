package com.example.toeicwebsite.domain.user.repository;

import com.example.toeicwebsite.domain.user.model.UserAiConfig;
import com.example.toeicwebsite.domain.user.model.UserId;

import java.util.Optional;

public interface UserAiConfigRepository {
    UserAiConfig save(UserAiConfig config);
    Optional<UserAiConfig> findByUserIdAndProvider(UserId userId, String provider);
    void deleteByUserIdAndProvider(UserId userId, String provider);
}
