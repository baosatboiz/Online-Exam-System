package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.assembler.ExamAssembler;
import com.example.toeicwebsite.application.command.CreateExamCommand;
import com.example.toeicwebsite.application.result.CreateExamResult;
import com.example.toeicwebsite.application.usecase.CreateExam;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateExamImpl implements CreateExam {
    private final ExamAssembler examAssembler;
    private final ExamRepository examRepository;
    @Override
    public CreateExamResult execute(CreateExamCommand command) {
        Exam exam = examAssembler.from(command);
        Exam savedExam = examRepository.save(exam);
        return new CreateExamResult(savedExam.getExamId(),savedExam.getTitle(),savedExam.getTotalQuestions());
    }
}
