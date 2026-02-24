package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.LoginCommand;
import com.example.toeicwebsite.application.result.LoginResult;
import com.example.toeicwebsite.application.usecase.Login;
import com.example.toeicwebsite.web.dto.login.LoginRequest;
import com.example.toeicwebsite.web.dto.login.LoginResponse;
import com.example.toeicwebsite.web.mapper.auth.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthMapper mapper;
    private final Login login;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse>  login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(mapper.toResponse(login.execute(mapper.toCommand(request))));

    }
}
