package com.example.toeicwebsite.domain.exam.model;

import java.util.List;

public class Question {
    private int number;
    private String content;
    private List<Choice> choices;
    private String explaination;

    public Question(int number, String content, List<Choice> choices,String explaination) {
        this.number = number;
        this.choices = choices;
        this.content = content;
        this.explaination = explaination;
    }
    public boolean hasChoice(ChoiceKey choiceKey){
        return choices.stream().anyMatch(choice -> choice.getKey().equals(choiceKey));
    }
    public String content() {
        return content;
    }

    public int number(){ return number;}
    public Choice getCorrectChoice(){
        return choices.stream().filter(Choice::isCorrect).findFirst().orElse(null);
    }
}
