package com.example.toeicwebsite.infrastucture.external.ai.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.example.toeicwebsite.infrastucture.external.ai.AbstractAiProvider;
import com.example.toeicwebsite.infrastucture.external.ai.PromptTemplateService;

@Slf4j
public class GroqProvider extends AbstractAiProvider {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String MODEL = "llama-3.3-70b-versatile";

    public GroqProvider(
            RestTemplate restTemplate,
            String apiKey,
            ObjectMapper objectMapper,
            PromptTemplateService promptTemplateService
    ) {
        super(
                restTemplate,
                apiKey,
                objectMapper,
                promptTemplateService
        );
    }

    @Override
    protected String getApiUrl() {
        return API_URL;
    }

    @Override
    protected String getModel() {
        return MODEL;
    }

    @Override
    protected HttpHeaders buildHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth(apiKey);

        return headers;
    }

    @Override
    public String getProviderName() {
        return "groq";
    }
}