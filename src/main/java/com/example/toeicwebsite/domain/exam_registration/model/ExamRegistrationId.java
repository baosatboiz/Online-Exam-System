package com.example.toeicwebsite.domain.exam_registration.model;

import java.util.UUID;

public class ExamRegistrationId{
    private UUID id;

    public UUID value() {
        return id;
    }
    public ExamRegistrationId(UUID id) {
        this.id = id;
    }
    public static ExamRegistrationId newId(){ return new ExamRegistrationId(UUID.randomUUID()); }
    @Override
    public boolean equals(Object o) {
        return o instanceof ExamRegistrationId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
