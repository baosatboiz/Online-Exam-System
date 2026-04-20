package com.example.toeicwebsite.application.query;

import com.example.toeicwebsite.domain.user.model.UserId;

public record GetVocabularySetsQuery(
        UserId userId
) {
}
