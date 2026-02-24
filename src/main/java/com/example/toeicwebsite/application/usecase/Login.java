package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.LoginCommand;
import com.example.toeicwebsite.application.result.LoginResult;

public interface Login {
    LoginResult execute(LoginCommand command);
}
