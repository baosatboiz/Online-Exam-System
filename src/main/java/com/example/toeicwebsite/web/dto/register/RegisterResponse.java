package com.example.toeicwebsite.web.dto.register;

import java.util.List;

public record RegisterResponse(
        String email,
        String authToken,
        List<String> roles
) {

}
