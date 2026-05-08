package com.example.toeicwebsite.infrastucture.external.ai;

import org.springframework.stereotype.Service;

@Service
public class PromptTemplateService {

    public String buildVocabularyPrompt(String word) {

        return """
            Generate vocabulary details for the word '%s'.

            IMPORTANT:
            Return ONLY a valid JSON object.
            No markdown.
            No code blocks.
            No extra text.

            JSON structure:
            {
              "word": "%s",
              "meaning": "short definition in both English and Vietnamese separated by a slash",
              "example": "one example sentence using the word",
              "note": "phrasal verbs, collocations or usage notes in Vietnamese"
            }
            """.formatted(word, word);
    }
}
