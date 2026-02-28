package com.example.toeicwebsite.web.dto.start_exam.request;

import java.util.UUID;

public record StartExamAttemptRequest(
        UUID examScheduleId
) {
}
