package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.LoginCommand;
import com.example.toeicwebsite.application.port.AuthPort;
import com.example.toeicwebsite.application.result.LoginResult;
import com.example.toeicwebsite.application.usecase.Login;
import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginImpl implements Login {
    private final UserRepository userRepository;
    private final AuthPort authPort;
    @Override
    public LoginResult execute(LoginCommand command) {
        authPort.authenticate(command.email(),command.password());
        User user = userRepository.findByEmail(command.email());
        String authToken = authPort.generateToken(command.email(),user.getUserRole());
        return new LoginResult(command.email(),authToken,user.getUserRole().stream().map(Enum::name).toList());
    }
}
