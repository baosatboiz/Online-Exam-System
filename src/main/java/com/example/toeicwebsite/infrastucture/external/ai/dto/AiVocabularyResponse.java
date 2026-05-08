package com.example.toeicwebsite.infrastucture.external.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AiVocabularyResponse(
        String word,
        String meaning,
        String example,
        String note
) {
}
