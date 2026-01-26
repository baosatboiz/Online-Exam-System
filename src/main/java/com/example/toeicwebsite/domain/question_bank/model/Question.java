package com.example.toeicwebsite.domain.question_bank.model;

import lombok.Getter;

import java.util.List;

public class Question {
    private Long questionId;
    private String content;
    private List<Choice> choices;
    private String explanation;
    @Getter
    private Choice correctChoice;

    public Question(Long questionId, String content, List<Choice> choices,String explanation) {
        this.questionId = questionId;
        this.choices = choices;
        this.content = content;
        this.explanation = explanation;
        this.correctChoice = findCorrectChoice(choices);
    }
    public boolean hasChoice(ChoiceKey choiceKey){
        return choices.stream().anyMatch(choice -> choice.getKey().equals(choiceKey));
    }
    public String content() {
        return content;
    }
    public Long id(){ return questionId;}

    private Choice findCorrectChoice(List<Choice> choices) {
        return choices.stream()
                .filter(Choice::isCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No correct choice"));
    }
}
