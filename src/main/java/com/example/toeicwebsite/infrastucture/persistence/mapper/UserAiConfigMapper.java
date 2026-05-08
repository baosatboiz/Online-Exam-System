package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.user.model.UserAiConfig;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.infrastucture.persistence.entity.UserAiConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserAiConfigMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "apiKeyEncrypted", source = "apiKeyEncrypted")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserAiConfig toDomain(UserAiConfigEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "apiKeyEncrypted", source = "apiKeyEncrypted")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserAiConfigEntity toEntity(UserAiConfig domain);

    default UserId mapUserId(UUID userId) {
        return userId == null ? null : new UserId(userId);
    }

    default UUID mapUserId(UserId userId) {
        return userId == null ? null : userId.value();
    }

    default Instant map(LocalDateTime value) {
        return value == null ? null : value.toInstant(ZoneOffset.UTC);
    }

    default LocalDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}
