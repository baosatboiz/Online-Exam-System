package com.example.toeicwebsite.application.port;

import com.example.toeicwebsite.domain.user.model.Role;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

public interface AuthPort {
    void authenticate(String email,String password);
    String generateToken(String email, List<Role> roles);
}
