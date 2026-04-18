package com.example.toeicwebsite.web.dto.exam;

import java.util.UUID;

public record RegisterExamRequest(
    UUID scheduleId
) {}
