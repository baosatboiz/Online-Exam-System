package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.web.mapper.ExamMapper;
import com.example.toeicwebsite.web.dto.request.ExamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamMapper examService;

    @PostMapping
    public void createExam(@RequestBody ExamRequest request) {
        examService.createExam(request);
    }
}
