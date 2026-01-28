package com.example.toeicwebsite.web.dto.create_exam.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamRequest {
    private String title;
    private String description;
    @JsonProperty("duration_minutes")
    private Integer durationMinutes;
    @JsonProperty("total_questions")
    private Integer totalQuestions;
    @JsonProperty("question_groups")
    private List<QuestionGroupRequest> questionGroups;
}