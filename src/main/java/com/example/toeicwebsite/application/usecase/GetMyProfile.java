package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.GetMyProfileQuery;
import com.example.toeicwebsite.application.result.GetMyProfileResult;

public interface GetMyProfile {
    GetMyProfileResult execute(GetMyProfileQuery query);
}
