package com.example.toeicwebsite.domain.exam.model;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exam {
    private ExamId examId;
    private List<Part> part;
    private String title;
    private int duration;
    @Setter
    private Map<Integer, Question> questionCache = new HashMap<>();

    public Exam(ExamId examId, List<Part> part, String title, int duration, ExamMode mode) {
        this.examId = examId;
        this.part = part;
        this.title = title;
        this.duration = duration;
//        initializeQuestionCache();
    }
//    void initializeQuestionCache(){
//        int count = 1;
//        for (Part p : part) {
//            if (p.getQuestionGroups() == null) continue;
//
//            for (QuestionGroup q : p.getQuestionGroups()) {
//                if (q.getQuestions() == null) continue;
//
//                for (Question question : q.getQuestions()) {
//                    questionCache.put(count++, question);
//                }
//            }
//        }
//    }
    public Map<Integer,Question> getQuestionCache(){ return questionCache;}

    public int getDuration() {return duration;}

    public Question getQuestion(int number) {
        return questionCache.get(number);
    }
    public List<Part> getPart(){ return part;}
    public int getTotalQuestion(){ return questionCache.size();}
}
