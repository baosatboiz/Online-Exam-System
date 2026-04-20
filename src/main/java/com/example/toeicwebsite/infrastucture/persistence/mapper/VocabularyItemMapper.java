package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItem;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularyItemId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;
import com.example.toeicwebsite.infrastucture.persistence.entity.VocabularyItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VocabularyItemMapper {
    @Mapping(target = "vocabularyItemId", source = "id")
    @Mapping(target = "vocabularySetId", source = "setId")
    @Mapping(target = "userId", source = "userId")
    VocabularyItem toDomain(VocabularyItemEntity entity);

    @Mapping(target = "id", source = "vocabularyItemId")
    @Mapping(target = "setId", source = "vocabularySetId")
    @Mapping(target = "userId", source = "userId")
    VocabularyItemEntity toEntity(VocabularyItem domain);

    default VocabularyItemId map(UUID id) {
        return id == null ? null : new VocabularyItemId(id);
    }

    default UUID map(VocabularyItemId id) {
        return id == null ? null : id.value();
    }

    default VocabularySetId mapSet(UUID id) {
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
