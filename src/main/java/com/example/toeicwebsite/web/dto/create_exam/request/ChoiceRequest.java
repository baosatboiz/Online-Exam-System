package com.example.toeicwebsite.web.dto.create_exam.request;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChoiceRequest {
    private ChoiceKey label;
    private String content;
    @JsonProperty("is_correct")
    private Boolean isCorrect;
}
