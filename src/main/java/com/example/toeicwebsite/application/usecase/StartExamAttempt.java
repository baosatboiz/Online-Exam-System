package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.StartExamAttemptCommand;
import com.example.toeicwebsite.application.result.StartExamAttemptResult;

public interface StartExamAttempt {
    StartExamAttemptResult execute(StartExamAttemptCommand command);
}
