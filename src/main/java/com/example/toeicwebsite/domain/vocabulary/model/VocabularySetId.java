package com.example.toeicwebsite.domain.vocabulary.model;

import java.util.UUID;

public class VocabularySetId {
    private final UUID id;

    public VocabularySetId(UUID id) {
        this.id = id;
    }

    public UUID value() {
        return id;
    }

    public static VocabularySetId newId() {
        return new VocabularySetId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof VocabularySetId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
