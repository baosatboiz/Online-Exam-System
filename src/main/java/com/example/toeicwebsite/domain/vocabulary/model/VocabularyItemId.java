package com.example.toeicwebsite.domain.vocabulary.model;

import java.util.UUID;

public class VocabularyItemId {
    private final UUID id;

    public VocabularyItemId(UUID id) {
        this.id = id;
    }

    public UUID value() {
        return id;
    }

    public static VocabularyItemId newId() {
        return new VocabularyItemId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof VocabularyItemId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
