package com.example.toeicwebsite.domain.exam_registration.service;

import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.repository.ExamRegistrationRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationPolicy {
    private final ExamRegistrationRepository examRegistrationRepository;
    public void validateRegistration(ExamSchedule examSchedule, User user){
        if(examRegistrationRepository.existsByUserIdAndScheduleId(user.getUserId(),examSchedule.getExamScheduleId())){
            throw new BusinessRuleException("You have registered this schedule before");
        }
        long currentCount = examRegistrationRepository.countByScheduleId(examSchedule.getExamScheduleId());
        if(examSchedule.isFull(currentCount)) throw new BusinessRuleException("This schedule is full of slots");
        if(examRegistrationRepository.existsOverlapping(user.getUserId(),examSchedule.getOpenAt(),examSchedule.getEndAt())){
            throw new BusinessRuleException("You're having overlapping schedule");
        }
    }
}
