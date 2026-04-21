package com.example.toeicwebsite.infrastucture.security.config;

import com.example.toeicwebsite.application.port.AuthPort;
import com.example.toeicwebsite.domain.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthAdapter implements AuthPort {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Override
    public void authenticate(String email, String password) {
        System.out.println("DEBUG: Password gửi vào Manager là: [" + password + "]");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @Override
    public String generateToken(String email, List<Role> roles) {
        return jwtUtils.generateToken(email,roles.stream().map(Enum::name).toList());
    }

    @Override
    public String generateToken(String email, List<Role> roles, String picture) {
        return jwtUtils.generateToken(email,roles.stream().map(Enum::name).toList(),picture);

    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
