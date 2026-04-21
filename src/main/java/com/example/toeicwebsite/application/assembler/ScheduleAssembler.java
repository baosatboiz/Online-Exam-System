package com.example.toeicwebsite.application.assembler;

import com.example.toeicwebsite.application.result.GetScheduleResult;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ScheduleAssembler {
    public List<GetScheduleResult> toResultList(
            List<ExamSchedule> schedules,
            Map<ExamId,Exam> exams,
            Map<ExamScheduleId,Long> totalAttempts,
            Map<ExamScheduleId, ExamStatus> userStatus,
            Map<ExamScheduleId, Boolean> userRegistrationStatus
    ){
        return schedules.stream()
                .filter(s -> exams.get(s.getExamId()) != null)
                .map(s->toResult(
                        s,
                        exams.get(s.getExamId()),
                        totalAttempts != null ? totalAttempts.getOrDefault(s.getExamScheduleId(), 0L) : 0L,
                        userStatus != null ? userStatus.get(s.getExamScheduleId()) : ExamStatus.NOT_STARTED,
                        userRegistrationStatus != null ? userRegistrationStatus.getOrDefault(s.getExamScheduleId(), false) : false
                )).toList();
    }
    private GetScheduleResult toResult(ExamSchedule schedule,Exam exam, Long totalAttempts, ExamStatus status, boolean isRegistered){
        return new GetScheduleResult(
                schedule.getExamScheduleId(),
                exam.getTitle(),
                exam.getDuration(),
                schedule.getOpenAt(),
                schedule.getEndAt(),
                totalAttempts,
                status!=null?status:ExamStatus.NOT_STARTED,
                schedule.getMode(),
                schedule.getPartType(),
                isRegistered);
    }
}
