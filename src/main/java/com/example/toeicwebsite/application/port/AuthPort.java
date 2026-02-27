package com.example.toeicwebsite.application.port;

import com.example.toeicwebsite.domain.user.model.Role;
import com.example.toeicwebsite.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

public interface AuthPort {
    void authenticate(String email,String password);
    String generateToken(String email, List<Role> roles);
    String encodePassword(String password);
}
