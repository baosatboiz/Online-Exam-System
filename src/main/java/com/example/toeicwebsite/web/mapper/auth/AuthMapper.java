package com.example.toeicwebsite.web.mapper.auth;

import com.example.toeicwebsite.application.command.LoginCommand;
import com.example.toeicwebsite.application.result.LoginResult;
import com.example.toeicwebsite.web.dto.login.LoginRequest;
import com.example.toeicwebsite.web.dto.login.LoginResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    LoginCommand toCommand(LoginRequest request);
    LoginResponse toResponse(LoginResult result);
}
