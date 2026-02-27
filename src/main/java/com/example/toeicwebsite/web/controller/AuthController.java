package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.usecase.Login;
import com.example.toeicwebsite.application.usecase.Register;
import com.example.toeicwebsite.web.dto.login.LoginRequest;
import com.example.toeicwebsite.web.dto.login.LoginResponse;
import com.example.toeicwebsite.web.dto.register.RegisterRequest;
import com.example.toeicwebsite.web.mapper.auth.AuthMapper;
import jakarta.validation.Valid;
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
    private final Register register;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse>  login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(mapper.toResponse(login.execute(mapper.toCommand(request))));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
            if(!request.confirmPassword().equals(request.password())) {
                return ResponseEntity.badRequest().body("Confirm password doesn't match");
            }
            return ResponseEntity.ok(mapper.toResponse(register.handle(mapper.toCommand(request))));
    }
}
