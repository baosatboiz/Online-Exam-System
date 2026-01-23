package com.example.toeicwebsite.domain.exam.model;

import java.util.List;

public class Question {
    private Long questionId;
    private String content;
    private List<Choice> choices;
    private String explaination;
    private Choice correctChoice;

    public Question(Long questionId,int number, String content, List<Choice> choices,String explaination,Choice correctChoice ) {
        this.questionId = questionId;
        this.choices = choices;
        this.content = content;
        this.explaination = explaination;
        this.correctChoice = correctChoice;
    }
    public boolean hasChoice(ChoiceKey choiceKey){
        return choices.stream().anyMatch(choice -> choice.getKey().equals(choiceKey));
    }
    public String content() {
        return content;
    }
    public Long id(){ return questionId;}

    public Choice getCorrectChoice(){
        return correctChoice;
            }
}
