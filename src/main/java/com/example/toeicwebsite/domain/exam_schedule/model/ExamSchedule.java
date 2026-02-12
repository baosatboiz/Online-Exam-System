package com.example.toeicwebsite.domain.exam_schedule.model;

import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exception.DomainException;
import lombok.Getter;

import java.time.Instant;
@Getter
public class ExamSchedule {
    private ExamScheduleId examScheduleId;
    private ExamId examId;
    private Instant openAt;
    private Instant endAt;
    private ExamMode mode;

    public ExamSchedule(ExamScheduleId examScheduleId, ExamId examId, Instant openAt, Instant endAt, ExamMode mode) {
        if(mode==ExamMode.REAL){
            if(openAt==null || endAt==null||openAt.isAfter(endAt)) throw new DomainException("Invalid time schedule");
        }
        this.examScheduleId = examScheduleId;
        this.examId = examId;
        this.openAt = openAt;
        this.endAt = endAt;
        this.mode = mode;
    }

    public boolean canStart(Instant now){
        if(isReal()) return !now.isBefore(openAt) && !now.isAfter(endAt);
        return true;
    }
    public ExamScheduleId id(){ return examScheduleId;};
    boolean isPractice(){ return mode==ExamMode.PRACTICE;};
    boolean isReal(){ return mode==ExamMode.REAL;};
    public Instant calculateMustFinishedAt(Instant startedAt, int duration){
        if(mode==ExamMode.PRACTICE){
            return startedAt.plusSeconds(duration * 60L);
        }
        return endAt;
    }
}
