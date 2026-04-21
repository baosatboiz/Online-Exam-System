package com.example.toeicwebsite.domain.user.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class UserProvider {
    private Long id;
    private UserId userId;
    private Provider provider;
    private String providerId;

    public UserProvider(Long id, UserId userId, Provider provider, String providerId) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.provider = Objects.requireNonNull(provider);
        this.providerId = providerId;
    }

    public static UserProvider createLocal(UserId userId) {
        return new UserProvider(null, userId, Provider.LOCAL, null);
    }

    public static UserProvider createGoogle(UserId userId, String googleSub) {
        return new UserProvider(null, userId,
                Provider.GOOGLE,
                Objects.requireNonNull(googleSub, "Google sub cannot be null"));
    }
}