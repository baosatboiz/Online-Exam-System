package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.CreateExamCommand;
import com.example.toeicwebsite.application.result.CreateExamResult;

public interface CreateExam {
    CreateExamResult execute(CreateExamCommand command);
}
