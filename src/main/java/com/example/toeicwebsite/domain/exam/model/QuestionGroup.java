package com.example.toeicwebsite.domain.exam.model;

import java.util.List;

public class QuestionGroup {
        private Long id;
        private String audioUrl;
        private String passage;
        private String imageUrl;

        private List<Question> questions;
        public List<Question> getQuestions(){
            return questions;
        }
}
