package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.CreateExamCommand;
import com.example.toeicwebsite.application.command.CreateScheduleCommand;
import com.example.toeicwebsite.application.command.DeleteScheduleCommand;
import com.example.toeicwebsite.application.query.GetScheduleQuery;
import com.example.toeicwebsite.application.usecase.CreateSchedule;
import com.example.toeicwebsite.application.usecase.DeleteSchedule;
import com.example.toeicwebsite.application.usecase.GetSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.infrastucture.security.config.SecurityUser;
import com.example.toeicwebsite.web.dto.create_schedule.CreateScheduleRequest;
import com.example.toeicwebsite.web.dto.create_schedule.CreateScheduleResponse;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleRequest;
import com.example.toeicwebsite.web.dto.get_exam_schedule.GetScheduleResponse;
import com.example.toeicwebsite.web.mapper.schedule_mapper.CreateScheduleMapper;
import com.example.toeicwebsite.web.mapper.schedule_mapper.GetScheduleMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exam-schedule")
@RequiredArgsConstructor
public class ExamScheduleController {
    private final GetScheduleMapper getScheduleMapper;
    private final GetSchedule getSchedule;
    private final CreateSchedule createSchedule;
    private final CreateScheduleMapper createScheduleMapper;
    private final DeleteSchedule deleteSchedule;
    @GetMapping
    public ResponseEntity<List<GetScheduleResponse>>  getExamSchedules(GetScheduleRequest request,
                                                                       @AuthenticationPrincipal SecurityUser securityUser){
        UserId userId= securityUser.getUser().getUserId();
        GetScheduleQuery query = getScheduleMapper.toQuery(request,userId);
        List<GetScheduleResponse> response = getSchedule.handle(query).stream().map(getScheduleMapper::toResponse).toList();
        return ResponseEntity.ok(response);
        }
    @PostMapping
    public ResponseEntity<CreateScheduleResponse> createExamSchedule(@RequestBody @Valid CreateScheduleRequest request){
        CreateScheduleCommand command = createScheduleMapper.toCommand(request);
        return ResponseEntity.ok(new CreateScheduleResponse(createSchedule.execute(command).examScheduleId().value()));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamSchedule(@PathVariable UUID id) {
        DeleteScheduleCommand command = new DeleteScheduleCommand(new ExamScheduleId(id));

        deleteSchedule.execute(command);

        return ResponseEntity.noContent().build();
    }

}
