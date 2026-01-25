package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.StartExamAttemptCommand;
import com.example.toeicwebsite.application.result.StartExamAttemptResult;
import com.example.toeicwebsite.application.usecase.StartExamAttempt;
import com.example.toeicwebsite.web.dto.request.StartExamAttemptRequest;
import com.example.toeicwebsite.web.dto.response.StartExamAttemptResponse;
import com.example.toeicwebsite.web.mapper.StartExamAttemptResponseMapper;
import com.example.toeicwebsite.web.mapper.StartExamAttemptWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exam-attempts")
@RequiredArgsConstructor
public class StartExamAttemptController {
    private final StartExamAttempt startExamAttempt;

    @PostMapping
    public ResponseEntity<StartExamAttemptResponse> startExamAttempt(@RequestBody StartExamAttemptRequest request) {
        String userId = "1";
        StartExamAttemptCommand command = StartExamAttemptWebMapper.toCommand(request, userId);
        StartExamAttemptResult result = startExamAttempt.execute(command);
        return ResponseEntity.ok(StartExamAttemptResponseMapper.from(result));
    }
}
