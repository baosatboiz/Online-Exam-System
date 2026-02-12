package com.example.toeicwebsite.infrastucture.persistence.projection;

import java.util.UUID;

public interface TotalAttemtpProjection {
    UUID getExamScheduleId();
    Long getTotal();
}
