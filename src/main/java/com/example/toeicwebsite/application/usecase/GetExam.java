package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.result.GetExamResult;

import java.util.List;

public interface GetExam {
    List<GetExamResult> handle();
}
