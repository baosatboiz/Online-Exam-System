package com.example.toeicwebsite.domain.exam_schedule.model;

import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.Getter;

import java.time.Instant;
import java.util.Optional;

@Getter
public class ExamSchedule {
    private ExamScheduleId examScheduleId;
    private ExamId examId;
    private Instant openAt;
    private Instant endAt;
    private ExamMode mode;
    private PartType partType;
    private Integer maxSlot;

    public ExamSchedule(ExamScheduleId examScheduleId, ExamId examId, Instant openAt, Instant endAt, ExamMode mode, PartType partType,Integer maxSlot) {

        if (mode == ExamMode.REAL) {
            if (openAt == null || endAt == null || openAt.isAfter(endAt))
                throw new BusinessRuleException("Invalid time schedule");
            if (maxSlot == null || maxSlot <= 0)
                throw new BusinessRuleException("Real exam must have positive max slots");
        }
        this.examScheduleId = examScheduleId;
        this.examId = examId;
        this.openAt = openAt;
        this.endAt = endAt;
        this.mode = mode;
        this.partType = partType;
        this.maxSlot=maxSlot;
    }
    public static ExamSchedule createPractice(ExamId examId, PartType partType) {
        return new ExamSchedule(ExamScheduleId.newId(),examId,null,null,ExamMode.PRACTICE,partType,null);
    }
    public static ExamSchedule createReal(ExamId examId,Instant openAt,Instant endAt, PartType partType, Integer maxSlot) {
        return new ExamSchedule(ExamScheduleId.newId(),examId,openAt,endAt,ExamMode.REAL,partType,maxSlot);
    }

    public void canStart(UserId userId, Optional<ExamRegistration> registration, Instant now) {
        if (!isReal()) return;

        if (now.isBefore(openAt) || now.isAfter(endAt)) {
            throw new BusinessRuleException("Exam is not in opening time");
        }

        ExamRegistration confirmedReg = registration
                .filter(ExamRegistration::isConfirmed)
                .orElseThrow(() -> new BusinessRuleException("Confirmed registration required for real exam"));

        if (!confirmedReg.getUserId().equals(userId)) {
            throw new BusinessRuleException("Registration does not belong to this user");
        }

        if (!confirmedReg.getExamScheduleId().equals(this.examScheduleId)) {
            throw new BusinessRuleException("Registration does not belong to this schedule");
        }
    }

    public ExamScheduleId id(){ return examScheduleId;};
    boolean isPractice(){ return mode==ExamMode.PRACTICE;}
    boolean isReal(){ return mode==ExamMode.REAL;};
    public boolean isFull(long count){
        if (!isReal() || this.maxSlot == null) {
            return false;
        }
        return count >= this.maxSlot;
    }
    public Instant calculateMustFinishedAt(Instant startedAt, int duration){
        if(mode==ExamMode.PRACTICE){
            return startedAt.plusSeconds(duration * 60L);
        }
        return endAt;
    }
}
