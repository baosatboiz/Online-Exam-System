package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.OAuth2LoginCommand;
import com.example.toeicwebsite.application.port.AuthPort;
import com.example.toeicwebsite.application.result.OAuth2LoginResult;
import com.example.toeicwebsite.application.usecase.OAuth2Login;
import com.example.toeicwebsite.domain.user.model.Role;
import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.model.UserProvider;
import com.example.toeicwebsite.domain.user.repository.UserProviderRepository;
import com.example.toeicwebsite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2LoginImpl implements OAuth2Login {

    private final UserProviderRepository userProviderRepository;
    private final UserRepository userRepository;
    private final AuthPort authPort;

    @Override
    public OAuth2LoginResult execute(OAuth2LoginCommand command) {
        User user;
        UserProvider existingProvider = userProviderRepository.findByProviderAndProviderId(command.provider(), command.providerId());
        if (existingProvider != null) {
            // đã login google
            user = userRepository.findByBusinessId(existingProvider.getUserId().value());
        } else {
            User existingUser = userRepository.findByEmail(command.email());

            if (existingUser != null) {
                // email đã tồn tại (đăng ký LOCAL trước) -> link thêm Google provider
                user = existingUser;
            } else {
                // hoàn toàn mới -> tạo user mới (không có password)
                user = User.registerWithOAuth2(command.email(), List.of(Role.USER));
                user = userRepository.save(user);
            }

            // lưu UserProvider mới (GOOGLE)
            UserProvider newProvider = UserProvider.createGoogle(user.getUserId(), command.providerId());
            userProviderRepository.save(newProvider);
        }

        String token = authPort.generateToken(user.getEmail(), user.getUserRole(),command.picture());
        return new OAuth2LoginResult(
                user.getEmail(),
                token,
                user.getUserRole().stream().map(Enum::name).toList()
        );
    }
}
