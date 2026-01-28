package com.example.toeicwebsite.domain.exam.model;

import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;

import java.util.List;

public class Part {
    private PartType partType;
    private List<QuestionGroup> questionGroups;
    public Part(PartType partType, List<QuestionGroup> questionGroups) {
        this.partType =  partType;
        this.questionGroups = questionGroups;
    }
    public static Part create(PartType partType, List<QuestionGroup> questionGroups) {
        return new Part(partType, questionGroups);
    }
    public PartType type(){ return partType; }
    public List<QuestionGroup> getQuestionGroups() {
        return questionGroups;
    }

}
