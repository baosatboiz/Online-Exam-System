package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.user.model.Provider;
import com.example.toeicwebsite.domain.user.model.UserProvider;
import com.example.toeicwebsite.domain.user.repository.UserProviderRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaUserProviderRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.UserProviderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserProviderRepositoryImpl implements UserProviderRepository {

    private final JpaUserProviderRepository jpaUserProviderRepository;
    private final UserProviderMapper userProviderMapper;

    @Override
    public UserProvider findByProviderAndProviderId(Provider provider, String providerId) {
        return userProviderMapper.toDomain(jpaUserProviderRepository.findByProviderAndProviderId(provider.name(), providerId).orElse(null));
    }

    @Override
    public UserProvider save(UserProvider userProvider) {
        return userProviderMapper.toDomain(jpaUserProviderRepository.save(userProviderMapper.toEntity(userProvider)));
    }
}
