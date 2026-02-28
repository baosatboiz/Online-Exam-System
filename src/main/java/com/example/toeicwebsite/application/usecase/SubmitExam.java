package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.SubmitExamCommand;
import com.example.toeicwebsite.application.result.SubmitExamResult;

public interface SubmitExam {
    SubmitExamResult execute(SubmitExamCommand command);
}
