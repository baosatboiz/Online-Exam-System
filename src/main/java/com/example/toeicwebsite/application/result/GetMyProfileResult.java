package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.user.model.Role;
import com.example.toeicwebsite.domain.user.model.UserId;

import java.util.List;

public record GetMyProfileResult(
        String email,
        UserId businessId,
        List<Role> roles
) {
}
