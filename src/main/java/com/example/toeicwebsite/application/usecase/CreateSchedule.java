package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.CreateScheduleCommand;
import com.example.toeicwebsite.application.result.CreateScheduleResult;

public interface CreateSchedule {
    CreateScheduleResult execute(CreateScheduleCommand command);
}
