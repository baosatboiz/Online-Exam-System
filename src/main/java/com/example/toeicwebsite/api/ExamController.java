package com.example.toeicwebsite.api;

import com.example.toeicwebsite.dto.request.ExamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.toeicwebsite.service.ExamService;

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
