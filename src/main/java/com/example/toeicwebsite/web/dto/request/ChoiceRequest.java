package com.example.toeicwebsite.web.dto.request;

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
