package com.example.toeicwebsite.domain.question_bank.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class QuestionGroup {
        private Long id;
        private String audioUrl;
        private String passage;
        private String imageUrl;

        private List<Question> questions = new ArrayList<>();
        public List<Question> getQuestions(){
            return questions;
        }

    public QuestionGroup(Long id, String audioUrl, String passage, String imageUrl, List<Question> questions) {
        this.id = id;
        this.audioUrl = audioUrl;
        this.passage = passage;
        this.imageUrl = imageUrl;
        this.questions = questions;
    }
    public static QuestionGroup create(String audioUrl,String passage,String imageUrl,List<Question> questions){
            return new QuestionGroup(null,audioUrl,passage,imageUrl,questions);
    }
}
