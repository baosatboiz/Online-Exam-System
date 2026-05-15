package com.example.toeicwebsite.domain.exam.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ExamId {
    private UUID id;

    public ExamId(UUID id) {
        this.id = id;
    }
    public UUID value() {
        return id;
    }
    public static ExamId newId(){ return new ExamId(UUID.randomUUID()); }
    @Override
    public boolean equals(Object o) {
        return o instanceof ExamId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}