package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.OAuth2LoginCommand;
import com.example.toeicwebsite.application.result.OAuth2LoginResult;

public interface OAuth2Login {
    OAuth2LoginResult execute(OAuth2LoginCommand command);
}
