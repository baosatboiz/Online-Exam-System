package com.example.toeicwebsite.application.result;

import java.util.List;

public record OAuth2LoginResult(
        String email,
        String token,
        List<String> roles
) {
}
