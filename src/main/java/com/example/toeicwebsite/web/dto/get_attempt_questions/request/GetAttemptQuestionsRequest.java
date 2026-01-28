package com.example.toeicwebsite.web.dto.get_attempt_questions.request;

import java.util.UUID;

public record GetAttemptQuestionsRequest(
        UUID attemptId
) {
}
