package com.example.toeicwebsite.infrastucture.external.ai;

import com.example.toeicwebsite.infrastucture.external.ai.dto.AiVocabularyResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractAiProvider implements AiProvider {

    protected final RestTemplate restTemplate;
    protected final String apiKey;
    protected final ObjectMapper objectMapper;
    protected final PromptTemplateService promptTemplateService;

    protected AbstractAiProvider(
            RestTemplate restTemplate,
            String apiKey,
            ObjectMapper objectMapper,
            PromptTemplateService promptTemplateService
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.promptTemplateService = promptTemplateService;
    }

    protected abstract String getApiUrl();

    protected abstract String getModel();

    protected abstract HttpHeaders buildHeaders();

    @Override
    public Optional<AiVocabularyResponse> generateVocabulary(String word) {
        try {
            String prompt = promptTemplateService.buildVocabularyPrompt(word);

            Map<String, Object> requestBody = buildChatRequest(prompt, 500);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(requestBody, buildHeaders());

            String responseString = restTemplate.postForObject(
                    getApiUrl(),
                    request,
                    String.class
            );

            return parseVocabularyResponse(responseString, word);

        } catch (RestClientException e) {
            log.warn("{} API call failed for word: {}", getProviderName(), word, e);
            return Optional.empty();

        } catch (Exception e) {
            log.error("Error generating vocabulary from {} for word: {}", getProviderName(), word, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean validateApiKey() {
        try {
            Map<String, Object> requestBody = buildChatRequest("test", 10);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, buildHeaders());

            String responseString = restTemplate.postForObject(
                    getApiUrl(),
                    request,
                    String.class
            );

            JsonNode response = objectMapper.readTree(responseString);

            if (response == null) {
                return false;
            }

            if (response.has("error")) {
                log.warn("{} API returned error: {}", getProviderName(), response.get("error").get("message").asText());
                return false;
            }

            return response.has("choices") && !response.get("choices").isEmpty();

        } catch (Exception e) {
            log.warn("{} API key validation failed: {}", getProviderName(), e.getMessage());
            return false;
        }
    }

    protected Map<String, Object> buildChatRequest(
            String prompt,
            int maxTokens
    ) {
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("model", getModel());

        requestBody.put("messages", new Object[]{
                Map.of("role", "user", "content", prompt)
        });

        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", maxTokens);

        return requestBody;
    }

    protected Optional<AiVocabularyResponse> parseVocabularyResponse(
            String responseString,
            String word
    ) {
        try {
            JsonNode response = objectMapper.readTree(responseString);

            if (response == null
                    || !response.has("choices")
                    || response.get("choices").isEmpty()) {

                log.warn("Invalid response from {} API for word: {}", getProviderName(), word);

                return Optional.empty();
            }

            String content = response.get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

            String jsonContent = extractJson(content);

            JsonNode jsonNode = objectMapper.readTree(jsonContent);

            AiVocabularyResponse result =
                    objectMapper.treeToValue(
                            jsonNode,
                            AiVocabularyResponse.class
                    );

            return Optional.of(result);

        } catch (Exception e) {
            log.error("Failed to parse JSON for word '{}'", getProviderName(), word, e);

            return Optional.empty();
        }
    }

    protected String extractJson(String content) {

        if (content.contains("```json")) {
            int start = content.indexOf("```json") + 7;
            int end = content.lastIndexOf("```");

            if (end > start) {
                content = content.substring(start, end).trim();
            }
        }

        int jsonStart = content.indexOf('{');
        int jsonEnd = content.lastIndexOf('}');

        if (jsonStart >= 0 && jsonEnd > jsonStart) {
            return content.substring(jsonStart, jsonEnd + 1);
        }

        return content;
    }
}