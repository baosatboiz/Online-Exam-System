package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.CreateAiConfigCommand;
import com.example.toeicwebsite.application.result.CreateAiConfigResult;
import com.example.toeicwebsite.application.usecase.CreateAiConfig;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.user.model.UserAiConfig;
import com.example.toeicwebsite.domain.user.repository.UserAiConfigRepository;
import com.example.toeicwebsite.infrastucture.external.ai.AiProvider;
import com.example.toeicwebsite.infrastucture.external.ai.AiProviderFactory;
import com.example.toeicwebsite.infrastucture.security.encryption.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateAiConfigImpl implements CreateAiConfig {

    private final UserAiConfigRepository userAiConfigRepository;
    private final EncryptionService encryptionService;
    private final AiProviderFactory aiProviderFactory;

    @Override
    public CreateAiConfigResult execute(CreateAiConfigCommand command) {
        if (!isValidProvider(command.provider())) {
            throw new BusinessRuleException("Invalid AI provider: " + command.provider());
        }

        // Validate API key by creating a temporary provider instance
        AiProvider aiProvider = aiProviderFactory.createProvider(command.provider(), command.apiKey());
        if (!aiProvider.validateApiKey()) {
            String errorMsg = command.provider().equals("groq")
                    ? "Groq API key không hợp lệ. Vui lòng kiểm tra:\n" +
                    "1. API key đúng từ https://console.groq.com\n" +
                    "2. Kết nối mạng không bị chặn\n" +
                    "3. API key đó chưa hết hạn"
                    : "OpenRouter API key không hợp lệ. Vui lòng kiểm tra:\n" +
                    "1. API key đúng từ https://openrouter.ai\n" +
                    "2. Kết nối mạng không bị chặn\n" +
                    "3. API key đó có đủ credit";
            throw new BusinessRuleException(errorMsg);
        }

        // Encrypt and save
        String encryptedKey = encryptionService.encrypt(command.apiKey());
        UserAiConfig config = UserAiConfig.create(command.userId(), command.provider(), encryptedKey);
        UserAiConfig savedConfig = userAiConfigRepository.save(config);
        return new CreateAiConfigResult(savedConfig.getProvider(), savedConfig.getCreatedAt(), savedConfig.getUpdatedAt());
    }

    private boolean isValidProvider(String provider) {
        return provider != null && (provider.equalsIgnoreCase("groq") || provider.equalsIgnoreCase("openrouter"));
    }
}
