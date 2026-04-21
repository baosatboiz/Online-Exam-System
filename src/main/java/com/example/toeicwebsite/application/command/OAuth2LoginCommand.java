package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.user.model.Provider;

public record OAuth2LoginCommand(
        String email,
        String picture,
        String providerId,
        Provider provider
) {
}
