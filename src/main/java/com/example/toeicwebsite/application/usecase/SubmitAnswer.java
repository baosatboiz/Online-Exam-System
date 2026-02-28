package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.SubmitAnswerCommand;
import com.example.toeicwebsite.application.result.SubmitAnswerResult;

public interface SubmitAnswer {
    SubmitAnswerResult execute(SubmitAnswerCommand command);
}
