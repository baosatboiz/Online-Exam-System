package com.example.toeicwebsite.infrastucture.external.dictionary;

import com.example.toeicwebsite.infrastucture.external.dictionary.dto.DictionaryApiResponse;
import com.example.toeicwebsite.infrastucture.external.dictionary.dto.PronunciationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DictionaryApiClientImpl implements DictionaryApiClient {
    private static final String DICTIONARY_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private final RestTemplate restTemplate;

    @Override
    public Optional<PronunciationData> fetchPronunciation(String word) {
        if (word == null || word.isBlank()) {
            return Optional.empty();
        }

        try {
            String url = DICTIONARY_API_URL + word.toLowerCase().trim();
            DictionaryApiResponse[] responses = restTemplate.getForObject(url, DictionaryApiResponse[].class);

            if (responses == null || responses.length == 0) {
                log.debug("No results from Dictionary API for word: {}", word);
                return Optional.empty();
            }

            DictionaryApiResponse response = responses[0];
            if (response.phonetics() == null || response.phonetics().isEmpty()) {
                log.debug("No phonetics data for word: {}", word);
                return Optional.empty();
            }

            String text = null;
            String audio = null;

            for (DictionaryApiResponse.Phonetic phonetic : response.phonetics()) {
                if (text == null && phonetic.text() != null && !phonetic.text().isBlank()) {
                    text = phonetic.text();
                }

                if (audio == null && phonetic.audio() != null && !phonetic.audio().isBlank()) {
                    audio = phonetic.audio();
                }

                if (text != null && audio != null) {
                    break;
                }
            }

            if (text != null || audio != null) {
                return Optional.of(new PronunciationData(
                    text != null ? text : "",
                    audio != null ? audio : ""
                ));
            }

            log.debug("No valid phonetic data for word: {}", word);
            return Optional.empty();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.debug("Word not found: {}", word);
            } else {
                log.warn("HTTP error calling Dictionary API for word: {}", word, e);
            }
            return Optional.empty();
        } catch (RestClientException e) {
            log.warn("Error calling Dictionary API for word: {}", word, e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error in Dictionary API client for word: {}", word, e);
            return Optional.empty();
        }
    }
}
