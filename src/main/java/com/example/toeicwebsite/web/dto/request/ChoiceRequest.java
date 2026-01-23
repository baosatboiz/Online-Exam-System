package com.example.toeicwebsite.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChoiceRequest {
    private String label;
    private String content;
    @JsonProperty("is_correct")
    private Boolean isCorrect;
}
