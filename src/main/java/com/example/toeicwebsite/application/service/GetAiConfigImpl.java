package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.query.GetAiConfigQuery;
import com.example.toeicwebsite.application.result.GetAiConfigResult;
import com.example.toeicwebsite.application.usecase.GetAiConfig;
import com.example.toeicwebsite.domain.user.model.UserAiConfig;
import com.example.toeicwebsite.domain.user.repository.UserAiConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAiConfigImpl implements GetAiConfig {
    private final UserAiConfigRepository userAiConfigRepository;

    @Override
    public GetAiConfigResult handle(GetAiConfigQuery query) {
        UserAiConfig config = userAiConfigRepository.findByUserIdAndProvider(query.userId(), query.provider())
                .orElse(null);
        if (config == null) {
            return null;
        }
        return new GetAiConfigResult(
                config.getId(),
                config.getUserId(),
                config.getProvider(),
                config.getCreatedAt(),
                config.getUpdatedAt()
        );
    }
}
