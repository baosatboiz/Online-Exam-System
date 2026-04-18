package com.example.toeicwebsite.infrastucture.persistence.projection;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ExamScheduleProjection {
    // Các field của Schedule
    UUID getBusinessId();
    ExamMode getMode();
    LocalDateTime getOpenAt();
    LocalDateTime getEndAt();
    Integer getPartNumber();
    Integer getMaxSlot();
    BigDecimal getPrice();

    UUID getExamBusinessId();
}