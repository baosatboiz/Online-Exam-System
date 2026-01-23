package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.web.dto.request.ExamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.toeicwebsite.infrastucture.persistence.import_data.ExamService;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @PostMapping
    public void createExam(@RequestBody ExamRequest request) {
        examService.createExam(request);
    }
}
