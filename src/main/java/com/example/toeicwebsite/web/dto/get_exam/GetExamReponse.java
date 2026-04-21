package com.example.toeicwebsite.web.dto.get_exam;

import java.util.UUID;

public record GetExamReponse(
        UUID businessId,
        String title
) {
}
