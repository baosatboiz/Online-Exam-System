package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.CreateAiConfigCommand;
import com.example.toeicwebsite.application.result.CreateAiConfigResult;

public interface CreateAiConfig {
    CreateAiConfigResult execute(CreateAiConfigCommand command);
}
