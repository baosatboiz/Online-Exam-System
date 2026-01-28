package com.example.toeicwebsite.web.dto.create_exam.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    private String content;
    private String explanation;
    private List<ChoiceRequest> choices;
}
