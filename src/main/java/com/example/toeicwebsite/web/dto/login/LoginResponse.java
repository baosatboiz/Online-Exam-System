package com.example.toeicwebsite.web.dto.login;

import java.util.List;

public record LoginResponse(
        String email,
        List<String> roles
) {
}
