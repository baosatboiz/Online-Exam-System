package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;

public record CreateAiConfigCommand(
        UserId userId,
        String provider,
        String apiKey
) {
}
