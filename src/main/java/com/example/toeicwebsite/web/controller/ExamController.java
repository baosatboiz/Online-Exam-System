package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.CreateExamCommand;
import com.example.toeicwebsite.application.result.CreateExamResult;
import com.example.toeicwebsite.application.usecase.CreateExam;
import com.example.toeicwebsite.web.dto.create_exam.request.ExamRequest;
import com.example.toeicwebsite.web.dto.create_exam.response.CreateExamResponse;
import com.example.toeicwebsite.web.mapper.create_exam.CreateExamWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final CreateExam createExam;
    private final CreateExamWebMapper createExamWebMapper;

//    @PostMapping
//    public void createExam(@RequestBody ExamRequest request) {
//        examService.createExam(request);
//    }
    @PostMapping
    public ResponseEntity<CreateExamResponse> createExam(@RequestBody ExamRequest examRequest){
        CreateExamCommand command = createExamWebMapper.toCommand(examRequest);
        CreateExamResult result = createExam.execute(command);
        CreateExamResponse response = createExamWebMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
