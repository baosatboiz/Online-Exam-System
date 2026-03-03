package com.example.toeicwebsite.domain.user.repository;

import com.example.toeicwebsite.domain.user.model.User;

import java.util.UUID;

public interface UserRepository {
    User findByEmail(String email);
    User save(User newUser);
    User findByBusinessId(UUID id);
}
