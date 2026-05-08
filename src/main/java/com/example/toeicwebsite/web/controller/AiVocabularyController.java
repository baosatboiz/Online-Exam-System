package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.usecase.GenerateVocabulary;
import com.example.toeicwebsite.infrastucture.external.ai.dto.AiVocabularyResponse;
import com.example.toeicwebsite.infrastucture.security.config.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.toeicwebsite.web.dto.ai.GenerateVocabularyRequest;
import com.example.toeicwebsite.application.command.GenerateVocabularyCommand;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiVocabularyController {
    private final GenerateVocabulary generateVocabulary;

    @PostMapping("/generate-vocabulary")
    public ResponseEntity<AiVocabularyResponse> generateVocabulary(
            @RequestBody GenerateVocabularyRequest request,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        return ResponseEntity.ok(
                generateVocabulary.execute(
                        new GenerateVocabularyCommand(
                                request.word(),
                                request.provider(),
                                securityUser.getUser().getUserId()
                        )
                )
        );
    }
}
