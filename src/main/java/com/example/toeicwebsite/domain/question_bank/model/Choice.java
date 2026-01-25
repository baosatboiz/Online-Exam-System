package com.example.toeicwebsite.domain.question_bank.model;

public class Choice {
    private ChoiceKey key;
    private String content;
    private boolean correct;

    public Choice(ChoiceKey key, String content, boolean correct) {
        this.key = key;
        this.content = content;
        this.correct = correct;
    }

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
