package com.example.toeicwebsite.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionGroupRequest {
    private Integer part;
    @JsonProperty("group_no")
    private Integer groupNo;
    @JsonProperty("audio_url")
    private String audioUrl;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("passage_text")
    private String passageText;
    private List<QuestionRequest> questions;
}