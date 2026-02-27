package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.RegisterCommand;
import com.example.toeicwebsite.application.port.AuthPort;
import com.example.toeicwebsite.application.result.RegisterResult;
import com.example.toeicwebsite.application.usecase.Register;
import com.example.toeicwebsite.domain.user.model.Role;
import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterImpl implements Register {
    private final AuthPort authPort;
    private final UserRepository userRepository;
    @Override
    public RegisterResult handle(RegisterCommand command) {
        String encodedPassword = authPort.encodePassword(command.password());
        User user = User.register(command.email(),encodedPassword, List.of(Role.USER));
        User savedUser = userRepository.save(user);
        String token = authPort.generateToken(savedUser.getEmail(),savedUser.getUserRole());
        return new RegisterResult(
                savedUser.getEmail(),
                token,
                savedUser.getUserRole()
        );
    }
}
