package com.example.toeicwebsite.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    @JsonProperty("question_no")
    private Integer questionNo;
    private String content;
    private List<ChoiceRequest> choices;
}
