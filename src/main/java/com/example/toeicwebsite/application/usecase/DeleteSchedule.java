package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.DeleteScheduleCommand;

public interface DeleteSchedule {
    void execute(DeleteScheduleCommand command);
}
