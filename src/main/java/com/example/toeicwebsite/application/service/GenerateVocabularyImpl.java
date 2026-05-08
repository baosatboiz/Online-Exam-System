package com.example.toeicwebsite.application.service;

import org.springframework.stereotype.Service;

import com.example.toeicwebsite.application.command.GenerateVocabularyCommand; 
import com.example.toeicwebsite.application.usecase.GenerateVocabulary;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.user.model.UserAiConfig;
import com.example.toeicwebsite.domain.user.repository.UserAiConfigRepository;
import com.example.toeicwebsite.infrastucture.external.ai.AiProvider;
import com.example.toeicwebsite.infrastucture.external.ai.AiProviderFactory;
import com.example.toeicwebsite.infrastucture.external.ai.dto.AiVocabularyResponse;
import com.example.toeicwebsite.infrastucture.security.encryption.EncryptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenerateVocabularyImpl implements GenerateVocabulary {

  private final UserAiConfigRepository userAiConfigRepository;
    private final EncryptionService encryptionService;
    private final AiProviderFactory aiProviderFactory;

    @Override
    public AiVocabularyResponse execute(GenerateVocabularyCommand command) {

        String provider = resolveProvider(command.provider());

        UserAiConfig config = userAiConfigRepository.findByUserIdAndProvider(command.userId(), provider)
                .orElseThrow(() -> new BusinessRuleException("AI config not found for provider: " + provider));

        String decryptedKey = encryptionService.decrypt(config.getApiKeyEncrypted());

        AiProvider aiProvider = aiProviderFactory.createProvider(provider, decryptedKey);

        return aiProvider.generateVocabulary(command.word())
                .orElseThrow(() -> new BusinessRuleException("Failed to generate vocabulary"));
    }

    private String resolveProvider(String provider) {
        if (provider == null || provider.isBlank()) {
            return "groq";
        }
        return provider;
    }
}
