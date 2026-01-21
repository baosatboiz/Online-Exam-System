package com.example.toeicwebsite.domain.exam.model;

import com.example.toeicwebsite.domain.exam_attempt.exception.DomainException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Exam {
    private ExamId examId;
    private List<Part> part;
    private String title;
    private int duration;
    private Instant openAt;
    private Instant endAt;
    private ExamMode mode;

    public Exam(ExamId examId, List<Part> part, String title,int duration,ExamMode mode) {
        this.examId = examId;
        this.part = part;
        this.title = title;
        this.duration = duration;
        this.mode = mode;
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
    }

    boolean isPractice(){ return mode==ExamMode.PRACTICE;};
    boolean isReal(){ return mode==ExamMode.REAL;};
    public ExamId id(){ return examId;};
    public Question getQuestion(int number) {
        int count =0;
        for(Part p:part) for(int i=0;i<p.getQuestions().size();i++){
            if(count==number-1) {return p.getQuestions().get(i);}
            count++;
        }
        return null;
    }
    public List<Part> getPart(){ return part;}
    public int getTotalQuestion(){ return part.stream().flatMap(p->p.getQuestions().stream()).toList().size();};
    public boolean canStart(Instant now){
        if(isReal()) return !now.isBefore(openAt) && !now.isAfter(endAt);
        return true;
    }
    public Instant calculateMustFinishedAt(Instant startedAt){
        if(mode==ExamMode.PRACTICE){
            return startedAt.plusSeconds(duration);
        }
        return startedAt.plusSeconds(duration).isBefore(endAt) ? startedAt.plusSeconds(duration) : endAt;
    }
}
