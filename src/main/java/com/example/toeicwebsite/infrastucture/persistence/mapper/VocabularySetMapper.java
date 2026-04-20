package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySet;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;
import com.example.toeicwebsite.infrastucture.persistence.entity.VocabularySetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VocabularySetMapper {
    @Mapping(target = "vocabularySetId", source = "id")
    @Mapping(target = "userId", source = "userId")
    VocabularySet toDomain(VocabularySetEntity entity);

    @Mapping(target = "id", source = "vocabularySetId")
    @Mapping(target = "userId", source = "userId")
    VocabularySetEntity toEntity(VocabularySet domain);

    default VocabularySetId map(UUID id) {
        return id == null ? null : new VocabularySetId(id);
    }

    default UUID map(VocabularySetId id) {
        return id == null ? null : id.value();
    }

    default UserId mapUser(UUID userId) {
        return userId == null ? null : new UserId(userId);
    }

    default UUID map(UserId userId) {
        return userId == null ? null : userId.value();
    }

    default Instant map(LocalDateTime value) {
        return value == null ? null : value.toInstant(ZoneOffset.UTC);
    }

    default LocalDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}
