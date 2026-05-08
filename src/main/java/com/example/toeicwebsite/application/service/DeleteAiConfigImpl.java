package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.DeleteAiConfigCommand;
import com.example.toeicwebsite.application.usecase.DeleteAiConfig;
import com.example.toeicwebsite.domain.user.repository.UserAiConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteAiConfigImpl implements DeleteAiConfig {

    private final UserAiConfigRepository userAiConfigRepository;

    @Override
    public void execute(DeleteAiConfigCommand command) {
        userAiConfigRepository.deleteByUserIdAndProvider(command.userId(), command.provider());
    }
}
