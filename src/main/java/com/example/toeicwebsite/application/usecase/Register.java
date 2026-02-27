package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.RegisterCommand;
import com.example.toeicwebsite.application.result.RegisterResult;

public interface Register {
    RegisterResult handle(RegisterCommand command);
}
