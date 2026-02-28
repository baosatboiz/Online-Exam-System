package com.example.toeicwebsite.application.command;

public record RegisterCommand(
        String email,
        String password
) {

}
