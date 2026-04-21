package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.query.GetMyProfileQuery;
import com.example.toeicwebsite.application.result.LoginResult;
import com.example.toeicwebsite.application.usecase.GetMyProfile;
import com.example.toeicwebsite.application.usecase.Login;
import com.example.toeicwebsite.application.usecase.Register;
import com.example.toeicwebsite.infrastucture.security.config.SecurityUser;
import com.example.toeicwebsite.infrastucture.security.oauth2.OAuth2UserPrincipal;
import com.example.toeicwebsite.web.dto.login.GetMyProfileResponse;
import com.example.toeicwebsite.web.dto.login.LoginRequest;
import com.example.toeicwebsite.web.dto.login.LoginResponse;
import com.example.toeicwebsite.web.dto.register.RegisterRequest;
import com.example.toeicwebsite.web.mapper.auth.AuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthMapper mapper;
    private final Login login;
    private final Register register;
    private final GetMyProfile getMyProfile;
    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestBody LoginRequest request){
        LoginResult result = login.execute(mapper.toCommand(request));
        ResponseCookie cookie = ResponseCookie.from("access_token",result.authToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header("Set-Cookie",cookie.toString()).body(Collections.emptyMap());

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
            if(!request.confirmPassword().equals(request.password())) {
                return ResponseEntity.badRequest().body("Confirm password doesn't match");
            }
            return ResponseEntity.ok(mapper.toResponse(register.handle(mapper.toCommand(request))));
    }
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal SecurityUser user){
       GetMyProfileResponse response = mapper.toResponse(getMyProfile.execute(new GetMyProfileQuery(user.getUsername())));
       response.setPicture(user.getPicture());
       return ResponseEntity.ok(response);
    }
}
