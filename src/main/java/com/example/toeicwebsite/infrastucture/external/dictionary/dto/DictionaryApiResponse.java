package com.example.toeicwebsite.infrastucture.external.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DictionaryApiResponse(
        String word,
        List<Phonetic> phonetics
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Phonetic(
            String text,
            String audio
    ) {
    }
}
