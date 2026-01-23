package com.example.toeicwebsite.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChoiceRequest {
    private String label;
    private String content;
    @JsonProperty("is_correct")
    private Boolean isCorrect;
}
