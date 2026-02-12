package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.query.GetScheduleQuery;
import com.example.toeicwebsite.application.usecase.GetSchedule;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleRequest;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleResponse;
import com.example.toeicwebsite.web.mapper.get_schedule_mapper.GetScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-schedule")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
@RequiredArgsConstructor
public class ExamScheduleController {
    private final GetScheduleMapper getScheduleMapper;
    private final GetSchedule getSchedule;
    @GetMapping
    public ResponseEntity<List<GetScheduleResponse>>  getExamSchedules(GetScheduleRequest request){
        String userId="1";
        GetScheduleQuery query = getScheduleMapper.toQuery(request,userId);
        List<GetScheduleResponse> response = getSchedule.handle(query).stream().map(getScheduleMapper::toResponse).toList();
        return ResponseEntity.ok(response);
        }
}
