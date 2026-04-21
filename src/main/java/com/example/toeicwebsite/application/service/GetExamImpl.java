package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.result.GetExamResult;
import com.example.toeicwebsite.application.usecase.GetExam;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetExamImpl implements GetExam {
    private final ExamRepository examRepository;
    @Override
    public List<GetExamResult> handle() {
        return examRepository.findAll().stream().map(e->new GetExamResult(e.businessId(),e.title())).toList();
    }
}
