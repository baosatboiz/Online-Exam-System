package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.user.model.UserAiConfig;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.user.repository.UserAiConfigRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaUserAiConfigRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.UserAiConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserAiConfigRepositoryImpl implements UserAiConfigRepository {
    private final JpaUserAiConfigRepository jpaUserAiConfigRepository;
    private final UserAiConfigMapper mapper;

    @Override
    public UserAiConfig save(UserAiConfig config) {
        var entity = mapper.toEntity(config);
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        var saved = jpaUserAiConfigRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<UserAiConfig> findByUserIdAndProvider(UserId userId, String provider) {
        return jpaUserAiConfigRepository.findByUserIdAndProvider(userId.value(), provider)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByUserIdAndProvider(UserId userId, String provider) {
        jpaUserAiConfigRepository.deleteByUserIdAndProvider(userId.value(), provider);
    }
}
