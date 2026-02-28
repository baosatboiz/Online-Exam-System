package com.example.toeicwebsite.web.dto.login;

import java.util.List;

public record LoginResponse(
        String email,
        String authToken,
        List<String> roles
) {
}
