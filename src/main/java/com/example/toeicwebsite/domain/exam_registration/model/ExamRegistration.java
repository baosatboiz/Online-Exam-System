package com.example.toeicwebsite.domain.exam_registration.model;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ExamRegistration {
    private ExamRegistrationId examRegistrationId;
    private ExamScheduleId examScheduleId;
    private UserId userId;
    private RegistrationStatus registrationStatus;
    private Instant createAt;
    private Instant confirmAt;
    private Instant expiredAt;




    public ExamRegistration(ExamRegistrationId examRegistrationId, ExamScheduleId examScheduleId, UserId userId, RegistrationStatus registrationStatus,Instant createAt, Instant confirmAt,Instant expiredAt) {
        if (examRegistrationId == null || examScheduleId == null || userId == null || registrationStatus == null) {
            throw new BusinessRuleException("Registration fields cannot be null");
        }
        this.examRegistrationId = examRegistrationId;
        this.examScheduleId = examScheduleId;
        this.userId = userId;
        this.registrationStatus = registrationStatus;
        this.createAt = createAt;
        this.confirmAt = confirmAt;
        this.expiredAt = expiredAt;
    }

    public static ExamRegistration createPending(ExamScheduleId examScheduleId, UserId userId) {
        return new ExamRegistration(ExamRegistrationId.newId(), examScheduleId, userId, RegistrationStatus.PENDING,Instant.now(),null,null);
    }

    public boolean isConfirmed(){
        return registrationStatus == RegistrationStatus.CONFIRMED;
    }

    public void confirm(){
        if(this.registrationStatus!=RegistrationStatus.PENDING) throw new BusinessRuleException("Only pending registration can be confirmed");
        this.registrationStatus=RegistrationStatus.CONFIRMED;
        this.confirmAt=Instant.now();
    }

    public void expire(){
        if(this.registrationStatus!=RegistrationStatus.PENDING) {
            throw new BusinessRuleException("Only pending registration can be expired");
        }
        this.registrationStatus=RegistrationStatus.EXPIRED;
        this.expiredAt=Instant.now();
    }

    public void cancel(){
        if(this.registrationStatus!=RegistrationStatus.PENDING) {
            throw new BusinessRuleException("Only pending registration can be cancelled");
        }
        this.registrationStatus=RegistrationStatus.CANCELLED;
    }
}
