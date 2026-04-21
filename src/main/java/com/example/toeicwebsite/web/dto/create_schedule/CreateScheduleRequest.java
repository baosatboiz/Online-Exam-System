package com.example.toeicwebsite.web.dto.create_schedule;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateScheduleRequest(
        UUID examId,
        Instant openAt,
        Instant endAt,
        @NotNull(message = "Exam mode is required")
        String examMode,
        Integer partNumber
) {
}
