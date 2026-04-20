package com.example.toeicwebsite.domain.vocabulary.model;

import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class VocabularySet {
    private final VocabularySetId vocabularySetId;
    private final UserId userId;
    private final String name;
    private final String description;
    private final Instant createdAt;
    private final Instant updatedAt;

    public VocabularySet(VocabularySetId vocabularySetId,UserId userId,String name,String description,Instant createdAt,Instant updatedAt) {
        this.vocabularySetId = Objects.requireNonNull(vocabularySetId);
        this.userId = Objects.requireNonNull(userId);
        this.name = Objects.requireNonNull(name).trim();
        this.description = description == null ? null : description.trim();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static VocabularySet create(UserId userId, String name, String description) {
        return new VocabularySet(VocabularySetId.newId(), userId, name, description, null, null);
    }
}
