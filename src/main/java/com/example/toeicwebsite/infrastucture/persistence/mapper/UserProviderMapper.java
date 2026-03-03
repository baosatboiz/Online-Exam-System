package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.user.model.Provider;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.user.model.UserProvider;
import com.example.toeicwebsite.infrastucture.persistence.entity.UserProviderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserProviderMapper {

    @Mapping(source = "userBusinessId", target = "userId")
    @Mapping(source = "provider", target = "provider")
    UserProvider toDomain(UserProviderEntity entity);
    @Mapping(source = "userId", target = "userBusinessId")
    @Mapping(source = "provider", target = "provider")
    UserProviderEntity toEntity(UserProvider provider);
    default UserId map(UUID uuid) {
        return new UserId(uuid);
    }
    default UUID map(UserId userId) {
        return userId.value();
    }
    default Provider map(String provider) {
        try {
            return Provider.valueOf(provider);
        }
        catch (Exception e) {
            return Provider.LOCAL;
        }
    }
    default String map(Provider provider) {
        return provider != null ? provider.name() : "LOCAL";
    }
}
