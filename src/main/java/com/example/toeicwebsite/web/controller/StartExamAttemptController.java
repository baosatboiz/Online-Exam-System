package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.StartExamAttemptCommand;
import com.example.toeicwebsite.application.command.SubmitAnswerCommand;
import com.example.toeicwebsite.application.command.SubmitExamCommand;
import com.example.toeicwebsite.application.query.GetAttemptQuestionsQuery;
import com.example.toeicwebsite.application.query.ReviewExamAttemptQuery;
import com.example.toeicwebsite.application.result.*;
import com.example.toeicwebsite.application.usecase.*;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.web.dto.get_attempt_questions.request.GetAttemptQuestionsRequest;
import com.example.toeicwebsite.web.dto.get_attempt_questions.response.GetAttemptQuestionsResponse;
import com.example.toeicwebsite.web.dto.review_exam_attempt.response.ReviewExamAttemptResponse;
import com.example.toeicwebsite.web.dto.start_exam.request.StartExamAttemptRequest;
import com.example.toeicwebsite.web.dto.start_exam.response.StartExamAttemptResponse;
import com.example.toeicwebsite.web.dto.submit_answer.request.SubmitAnswerRequest;
import com.example.toeicwebsite.web.dto.submit_answer.response.SubmitAnswerResponse;
import com.example.toeicwebsite.web.dto.submit_exam.response.SubmitExamResponse;
import com.example.toeicwebsite.web.mapper.get_attempt_questions.GetAttemptQuestionsWebMapper;
import com.example.toeicwebsite.web.mapper.review_exam_attempt.ReviewExamAttemptWebMapper;
import com.example.toeicwebsite.web.mapper.start_exam.StartExamAttemptResponseMapper;
import com.example.toeicwebsite.web.mapper.start_exam.StartExamAttemptWebMapper;
import com.example.toeicwebsite.web.mapper.submit_answer.SubmitAnswerMapper;
import com.example.toeicwebsite.web.mapper.submit_exam.SubmitExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/exam-attempts")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
@RequiredArgsConstructor
public class StartExamAttemptController {
    private final StartExamAttempt startExamAttempt;
    private final GetAttemptQuestions getAttemptQuestions;
    private final SubmitAnswer submitAnswer;
    private final SubmitExam submitExam;
    private final ReviewExamAttempt reviewExamAttempt;
    private final StartExamAttemptResponseMapper startExamAttemptResponseMapper;
    private final StartExamAttemptWebMapper startExamAttemptWebMapper;
    private final GetAttemptQuestionsWebMapper getAttemptQuestionsWebMapper;
    private final SubmitAnswerMapper submitAnswerMapper;
    private final SubmitExamMapper submitExamMapper;
    private final ReviewExamAttemptWebMapper reviewExamAttemptWebMapper;

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

    @PostMapping("/{examAttemptId}/answer")
    public ResponseEntity<SubmitAnswerResponse> submitAnswer(@PathVariable UUID examAttemptId,
                                                             @RequestBody SubmitAnswerRequest request) {
        SubmitAnswerCommand command = submitAnswerMapper.toCommand(examAttemptId, request);
        SubmitAnswerResult result = submitAnswer.execute(command);
        return ResponseEntity.ok(submitAnswerMapper.toResponse(result));
    }

    @PutMapping("/{examAttemptId}/submit")
    public ResponseEntity<SubmitExamResponse> submitExam(@PathVariable UUID examAttemptId) {
        SubmitExamCommand command = new SubmitExamCommand(new ExamAttemptId(examAttemptId));
        SubmitExamResult result = submitExam.execute(command);
        long totalTimeSeconds = Duration
                .between(result.startedAt(), result.finishedAt())
                .toSeconds();
        return ResponseEntity.ok(submitExamMapper.toResponse(result, totalTimeSeconds));
    }

    @GetMapping("/{examAttemptId}/review")
    public ResponseEntity<ReviewExamAttemptResponse> reviewExamAttempt(@PathVariable UUID examAttemptId) {
        ReviewExamAttemptQuery query = new ReviewExamAttemptQuery(new ExamAttemptId(examAttemptId));
        ReviewExamAttemptResult result = reviewExamAttempt.handle(query);
        ReviewExamAttemptResponse response = reviewExamAttemptWebMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }
}
