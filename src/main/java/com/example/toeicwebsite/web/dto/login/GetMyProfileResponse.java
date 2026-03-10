package com.example.toeicwebsite.web.dto.login;

import java.util.List;

public record GetMyProfileResponse(
        String email,
        String businessId,
        List<String> roles

) {
}
