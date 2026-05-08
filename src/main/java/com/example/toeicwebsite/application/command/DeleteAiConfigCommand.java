package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.UserId;

public record DeleteAiConfigCommand(
        UserId userId,
        String provider
) {
}
