package com.example.toeicwebsite.application.command;

public record LoginCommand (
        String email,
        String password
){
}
