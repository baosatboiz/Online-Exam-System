package com.example.toeicwebsite.domain.exam.model;

import com.example.toeicwebsite.domain.exam_attempt.exception.DomainException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exam {
    private ExamId examId;
    private List<Part> part;
    private String title;
    private int duration;
    private Instant openAt;
    private Instant endAt;
    private ExamMode mode;
    private Map<Integer,Question> questionCache = new HashMap<>();
    public Exam(ExamId examId, List<Part> part, String title,int duration,ExamMode mode) {
        this(examId,part,title,duration,null,null,mode);
    }

    public Exam(ExamId examId, List<Part> part, String title, int duration, Instant openAt, Instant endAt, ExamMode mode) {
        if(mode==ExamMode.REAL){
            if(openAt==null||endAt==null) throw new DomainException("Real exam need setting open at and end at");
            if(endAt.isBefore(openAt)){ throw new DomainException("Invalid Date range");}
        }
        this.examId = examId;
        this.part = part;
        this.title = title;
        this.duration = duration;
        this.openAt = openAt;
        this.endAt = endAt;
        this.mode = mode;
        initializeQuestionCache();
    }
    void initializeQuestionCache(){
        int count = 1;
        for(Part p : part) for(QuestionGroup q : p.getQuestionGroups())
            for(Question question : q.getQuestions()){
               questionCache.put(count++,question);
        }
    }
    public Map<Integer,Question> getQuestionCache(){ return questionCache;}
    boolean isPractice(){ return mode==ExamMode.PRACTICE;};
    boolean isReal(){ return mode==ExamMode.REAL;};
    public ExamId id(){ return examId;};
    public Question getQuestion(int number) {
        return questionCache.get(number);
    }
    public List<Part> getPart(){ return part;}
    public int getTotalQuestion(){ return questionCache.size();}
    public boolean canStart(Instant now){
        if(isReal()) return !now.isBefore(openAt) && !now.isAfter(endAt);
        return true;
    }
    public Instant calculateMustFinishedAt(Instant startedAt){
        if(mode==ExamMode.PRACTICE){
            return startedAt.plusSeconds(duration);
        }
        return endAt;
    }
}
