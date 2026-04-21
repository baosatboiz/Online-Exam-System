package com.example.toeicwebsite.domain.exam_registration.service;

import com.example.toeicwebsite.domain.exam_registration.repository.ExamRegistrationRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RegistrationPolicy {
    private final ExamRegistrationRepository examRegistrationRepository;
    public void validateRegistration(ExamSchedule examSchedule, UserId userId){
        if(examRegistrationRepository.existsByUserIdAndScheduleId(userId,examSchedule.getExamScheduleId())){
            throw new BusinessRuleException("You have registered this schedule before");
        }
        if (examSchedule.getOpenAt() != null && Instant.now().isAfter(examSchedule.getOpenAt().minus(3, ChronoUnit.DAYS))) {
            throw new BusinessRuleException("Registration is closed (must register at least 3 days before exam)");
        }
        long currentCount = examRegistrationRepository.countByScheduleId(examSchedule.getExamScheduleId());
        if(examSchedule.isFull(currentCount)) throw new BusinessRuleException("This schedule is full of slots");
        if(examRegistrationRepository.existsOverlapping(userId,examSchedule.getOpenAt(),examSchedule.getEndAt())){
            throw new BusinessRuleException("You're having overlapping schedule");
        }
    }
}
