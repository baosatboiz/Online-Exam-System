package com.example.toeicwebsite.infrastucture.external.ai;

import com.example.toeicwebsite.infrastucture.external.ai.provider.GroqProvider;
import com.example.toeicwebsite.infrastucture.external.ai.provider.OpenRouterProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AiProviderFactory {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PromptTemplateService promptTemplateService;

    public AiProvider createProvider(String providerName, String decryptedApiKey) {
        return switch (providerName.toLowerCase()) {
            case "groq" -> new GroqProvider(restTemplate, decryptedApiKey, objectMapper, promptTemplateService);
            case "openrouter" -> new OpenRouterProvider(restTemplate, decryptedApiKey, objectMapper, promptTemplateService);
            default -> throw new IllegalArgumentException("Unknown AI provider: " + providerName);
        };
    }
}
