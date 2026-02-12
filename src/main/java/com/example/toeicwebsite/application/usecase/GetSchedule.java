package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.GetScheduleQuery;
import com.example.toeicwebsite.application.result.GetScheduleResult;

import java.util.List;

public interface GetSchedule {
    List<GetScheduleResult> handle(GetScheduleQuery query);
}
