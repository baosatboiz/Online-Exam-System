package com.example.toeicwebsite.infrastucture.external.dictionary;

import com.example.toeicwebsite.infrastucture.external.dictionary.dto.PronunciationData;

import java.util.Optional;

public interface DictionaryApiClient {
    Optional<PronunciationData> fetchPronunciation(String word);
}
