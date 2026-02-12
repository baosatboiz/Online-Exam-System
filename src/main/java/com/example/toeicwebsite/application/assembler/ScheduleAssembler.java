package com.example.toeicwebsite.application.assembler;

import com.example.toeicwebsite.application.result.GetScheduleResult;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamId;
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
            Map<ExamScheduleId, ExamStatus> userStatus
    ){
        return schedules.stream().map(s->toResult(s,exams.get(s.getExamId()),totalAttempts.get(s.getExamScheduleId()),userStatus.get(s.getExamScheduleId()))).toList();
    }
    private GetScheduleResult toResult(ExamSchedule schedule,Exam exam, Long totalAttempts, ExamStatus status){
        return new GetScheduleResult(
                schedule.getExamScheduleId(),
                exam.getTitle(),
                exam.getDuration(),
                schedule.getOpenAt(),
                schedule.getEndAt(),
                totalAttempts,
                status!=null?status:ExamStatus.NOT_STARTED);
    }
}
