package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.RegisterExamCommand;
import com.example.toeicwebsite.application.result.RegisterExamResult;

public interface RegisterExam {
    RegisterExamResult handle(RegisterExamCommand command);
}
