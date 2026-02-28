package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.user.model.Role;

import java.util.List;

public record RegisterResult(
        String email,
        String authToken,
        List<Role> roles
) {

}
