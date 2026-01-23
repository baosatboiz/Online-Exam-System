package com.example.toeicwebsite.domain.question_bank.model;

public class Choice {
    private ChoiceKey key;
    private String content;
    private boolean correct;

    public ChoiceKey getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public boolean isCorrect() {
        return correct;
    }
}
