package com.example.toeicwebsite.domain.user.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Getter
public class User {
    private UserId userId;
    private String email;
    private String password;
    private List<Role> userRole;
    public User(UserId userId, String email, String password,List<Role> userRole) {
        this.userId = Objects.requireNonNull(userId);
        this.email = Objects.requireNonNull(email,"Email cannot be null");
        this.password = Objects.requireNonNull(password,"Password cannot be null");
        this.userRole = userRole;
    }
    public void changePassword(String encodedPassword) {
        this.password = Objects.requireNonNull(encodedPassword,"Password cannot be null");
    }
}
