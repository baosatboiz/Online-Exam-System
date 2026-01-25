package com.example.toeicwebsite.domain.exam_attempt.model;

import java.util.UUID;

public class ExamAttemptId{
    private UUID id;

    public UUID value() {
        return id;
    }
    public ExamAttemptId(UUID id) {
        this.id = id;
    }
    public static ExamAttemptId newId(){ return new ExamAttemptId(UUID.randomUUID()); }
    @Override
    public boolean equals(Object o) {
        return o instanceof ExamAttemptId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
