package com.example.toeicwebsite.domain.user.model;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class UserAiConfig {
    private final String id;
    private final UserId userId;
    private final String provider;
    private final String apiKeyEncrypted;
    private final Instant createdAt;
    private final Instant updatedAt;

    public UserAiConfig(String id, UserId userId, String provider, String apiKeyEncrypted, 
                        Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.provider = Objects.requireNonNull(provider);
        this.apiKeyEncrypted = Objects.requireNonNull(apiKeyEncrypted);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserAiConfig create(UserId userId, String provider, String apiKeyEncrypted) {
        return new UserAiConfig(
            java.util.UUID.randomUUID().toString(),
            userId,
            provider,
            apiKeyEncrypted,
            null,
            null
        );
    }

}
