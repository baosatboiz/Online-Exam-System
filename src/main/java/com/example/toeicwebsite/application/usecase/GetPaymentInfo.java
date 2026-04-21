package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.result.GetPaymentInfoResult;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.user.model.UserId;

public interface GetPaymentInfo {
    GetPaymentInfoResult getPaymentInfo(UserId userId, ExamScheduleId examScheduleId);
}
