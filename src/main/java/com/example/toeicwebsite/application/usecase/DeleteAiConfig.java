package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.DeleteAiConfigCommand;

public interface DeleteAiConfig {
    void execute(DeleteAiConfigCommand command);
}
