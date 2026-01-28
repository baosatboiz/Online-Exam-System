package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.StartExamAttemptCommand;
import com.example.toeicwebsite.application.query.GetAttemptQuestionsQuery;
import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;
import com.example.toeicwebsite.application.result.StartExamAttemptResult;
import com.example.toeicwebsite.application.usecase.GetAttemptQuestions;
import com.example.toeicwebsite.application.usecase.StartExamAttempt;
import com.example.toeicwebsite.web.dto.get_attempt_questions.request.GetAttemptQuestionsRequest;
import com.example.toeicwebsite.web.dto.get_attempt_questions.response.GetAttemptQuestionsResponse;
import com.example.toeicwebsite.web.dto.start_exam.request.StartExamAttemptRequest;
import com.example.toeicwebsite.web.dto.start_exam.response.StartExamAttemptResponse;
import com.example.toeicwebsite.web.mapper.get_attempt_questions.GetAttemptQuestionsWebMapper;
import com.example.toeicwebsite.web.mapper.start_exam.StartExamAttemptResponseMapper;
import com.example.toeicwebsite.web.mapper.start_exam.StartExamAttemptWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/exam-attempts")
@RequiredArgsConstructor
public class StartExamAttemptController {
    private final StartExamAttempt startExamAttempt;
    private final GetAttemptQuestions getAttemptQuestions;
    private final StartExamAttemptResponseMapper startExamAttemptResponseMapper;
    private final StartExamAttemptWebMapper startExamAttemptWebMapper;
    private final GetAttemptQuestionsWebMapper getAttemptQuestionsWebMapper;

    @PostMapping
    public ResponseEntity<StartExamAttemptResponse> startExamAttempt(@RequestBody StartExamAttemptRequest request) {
        String userId = "1";
        StartExamAttemptCommand command = startExamAttemptWebMapper.toCommand(request, userId);
        StartExamAttemptResult result = startExamAttempt.execute(command);
        return ResponseEntity.ok(startExamAttemptResponseMapper.toResponse(result, Instant.now()));
    }

    @GetMapping("/{examAttemptId}/questions")
    public ResponseEntity<GetAttemptQuestionsResponse> getAttemptQuestions(@PathVariable UUID examAttemptId) {
        String userId = "1";
        GetAttemptQuestionsRequest request = new GetAttemptQuestionsRequest(examAttemptId);
        GetAttemptQuestionsQuery query = getAttemptQuestionsWebMapper.toQuery(request, userId);
        GetAttemptQuestionsResult result = getAttemptQuestions.handle(query);
        return ResponseEntity.ok(getAttemptQuestionsWebMapper.toResponse(result));
    }
}
