package com.example.toeicwebsite.application.result;

import java.time.Instant;
import java.util.List;

public record LoginResult(
        String email,
        String authToken,
        List<String> roles
) {
}
