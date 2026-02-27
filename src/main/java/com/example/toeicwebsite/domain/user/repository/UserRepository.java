package com.example.toeicwebsite.domain.user.repository;

import com.example.toeicwebsite.domain.user.model.User;

public interface UserRepository {
    User findByEmail(String email);
    User save(User newUser);
}
