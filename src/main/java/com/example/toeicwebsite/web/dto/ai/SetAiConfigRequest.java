package com.example.toeicwebsite.web.dto.ai;

import jakarta.validation.constraints.NotBlank;

public record SetAiConfigRequest(
        @NotBlank(message = "Provider is required")
        String provider,
        @NotBlank(message = "API key is required")
        String apiKey
) {
}
