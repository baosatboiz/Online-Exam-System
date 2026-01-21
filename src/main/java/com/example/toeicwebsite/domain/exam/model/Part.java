package com.example.toeicwebsite.domain.exam.model;

import java.util.List;

public class Part {
    private PartType partType;
    private List<Question> questions;
    Part(PartType partType, List<Question> questions) {
        this.partType =  partType;
        this.questions = questions;
    }
    public PartType type(){ return partType; }
    public List<Question> getQuestions() {
        return questions;
    }
}
