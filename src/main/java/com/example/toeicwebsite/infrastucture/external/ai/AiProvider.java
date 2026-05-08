package com.example.toeicwebsite.infrastucture.external.ai;

import com.example.toeicwebsite.infrastucture.external.ai.dto.AiVocabularyResponse;

import java.util.Optional;

public interface AiProvider {
    Optional<AiVocabularyResponse> generateVocabulary(String word);
    boolean validateApiKey();
    String getProviderName();
}
