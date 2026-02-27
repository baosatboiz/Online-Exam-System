package com.example.toeicwebsite.web.mapper.auth;

import com.example.toeicwebsite.application.command.LoginCommand;
import com.example.toeicwebsite.application.command.RegisterCommand;
import com.example.toeicwebsite.application.result.LoginResult;
import com.example.toeicwebsite.application.result.RegisterResult;
import com.example.toeicwebsite.domain.user.model.Role;
import com.example.toeicwebsite.web.dto.login.LoginRequest;
import com.example.toeicwebsite.web.dto.login.LoginResponse;
import com.example.toeicwebsite.web.dto.register.RegisterRequest;
import com.example.toeicwebsite.web.dto.register.RegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    LoginCommand toCommand(LoginRequest request);
    LoginResponse toResponse(LoginResult result);
    RegisterCommand toCommand(RegisterRequest request);
    RegisterResponse toResponse(RegisterResult result);
    default String map(Role role){
        return role.name();
    }
}
