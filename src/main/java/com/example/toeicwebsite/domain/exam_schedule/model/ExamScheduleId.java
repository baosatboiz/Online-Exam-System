package com.example.toeicwebsite.domain.exam_schedule.model;

import java.util.UUID;

public class ExamScheduleId {
    private UUID id;

    public ExamScheduleId(UUID id) {
        this.id = id;
    }
    public UUID value() {
        return id;
    }
    public static ExamScheduleId newId(){ return new ExamScheduleId(UUID.randomUUID()); }
    @Override
    public boolean equals(Object o) {
        return o instanceof ExamScheduleId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
