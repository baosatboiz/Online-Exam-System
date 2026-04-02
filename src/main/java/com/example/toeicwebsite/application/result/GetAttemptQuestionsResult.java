package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAttemptQuestionsResult {
    private String title;
    private int duration;
    private List<PartResult> parts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PartResult {
        private PartType partType;
        private List<QuestionGroupResult> questionGroups;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionGroupResult {
        private String audioUrl;
        private String passage;
        private String imageUrl;
        private List<QuestionResult> questions;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionResult {
        private Long questionId;
        private String content;
        private List<ChoiceResult> choices;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChoiceResult {
        private ChoiceKey key;
        private String content;
    }
}