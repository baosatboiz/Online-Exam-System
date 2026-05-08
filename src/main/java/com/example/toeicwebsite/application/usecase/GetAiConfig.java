package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.GetAiConfigQuery;
import com.example.toeicwebsite.application.result.GetAiConfigResult;

public interface GetAiConfig {
        GetAiConfigResult handle(GetAiConfigQuery query);
}
