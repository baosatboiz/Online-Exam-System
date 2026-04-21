package com.example.toeicwebsite.domain.user.repository;

import com.example.toeicwebsite.domain.user.model.Provider;
import com.example.toeicwebsite.domain.user.model.UserProvider;

public interface UserProviderRepository {
    UserProvider findByProviderAndProviderId(Provider provider, String providerId);
    UserProvider save(UserProvider userProvider);
}
