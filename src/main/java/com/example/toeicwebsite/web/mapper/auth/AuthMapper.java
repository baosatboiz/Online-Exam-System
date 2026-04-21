package com.example.toeicwebsite.web.mapper.auth;

import com.example.toeicwebsite.application.command.LoginCommand;
import com.example.toeicwebsite.application.command.RegisterCommand;
import com.example.toeicwebsite.application.result.GetMyProfileResult;
import com.example.toeicwebsite.application.result.LoginResult;
import com.example.toeicwebsite.application.result.RegisterResult;
import com.example.toeicwebsite.domain.user.model.Role;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.web.dto.login.GetMyProfileResponse;
import com.example.toeicwebsite.web.dto.login.LoginRequest;
import com.example.toeicwebsite.web.dto.login.LoginResponse;
import com.example.toeicwebsite.web.dto.register.RegisterRequest;
import com.example.toeicwebsite.web.dto.register.RegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    LoginCommand toCommand(LoginRequest request);
    LoginResponse toResponse(LoginResult result);
    GetMyProfileResponse toResponse(GetMyProfileResult result);
    RegisterCommand toCommand(RegisterRequest request);
    RegisterResponse toResponse(RegisterResult result);
    default String map(Role role){
        return role.name();
    }
    default String map(UserId businessId){
        return businessId.value().toString();
    }
}
