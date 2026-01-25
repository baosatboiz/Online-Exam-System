package com.example.toeicwebsite.web.dto.request;

import java.util.UUID;

public record StartExamAttemptRequest(
        UUID examScheduleId
) {
}
