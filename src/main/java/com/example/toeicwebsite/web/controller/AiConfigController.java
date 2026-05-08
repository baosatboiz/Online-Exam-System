package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.CreateAiConfigCommand;
import com.example.toeicwebsite.application.command.DeleteAiConfigCommand;
import com.example.toeicwebsite.application.query.GetAiConfigQuery;
import com.example.toeicwebsite.application.result.CreateAiConfigResult;
import com.example.toeicwebsite.application.result.GetAiConfigResult;
import com.example.toeicwebsite.application.usecase.CreateAiConfig;
import com.example.toeicwebsite.application.usecase.DeleteAiConfig;
import com.example.toeicwebsite.application.usecase.GetAiConfig;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.infrastucture.security.config.SecurityUser;
import com.example.toeicwebsite.web.dto.ai.AiConfigResponse;
import com.example.toeicwebsite.web.dto.ai.SetAiConfigRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ai-config")
@RequiredArgsConstructor
public class AiConfigController {
    private final CreateAiConfig createAiConfig;
    private final GetAiConfig getAiConfig;
    private final DeleteAiConfig deleteAiConfig;

    @PostMapping
    public ResponseEntity<AiConfigResponse> setAiConfig(
            @RequestBody @Valid SetAiConfigRequest request,
            @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        CreateAiConfigCommand command = new CreateAiConfigCommand(userId, request.provider(), request.apiKey());
        CreateAiConfigResult result = createAiConfig.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AiConfigResponse(result.provider(), result.createdAt(), result.updatedAt())
        );
    }

    @GetMapping("/{provider}")
    public ResponseEntity<AiConfigResponse> getAiConfig(
            @PathVariable String provider,
            @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        GetAiConfigQuery query = new GetAiConfigQuery(userId, provider);
        GetAiConfigResult config = getAiConfig.handle(query);
        
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(
                new AiConfigResponse(config.provider(), config.createdAt(), config.updatedAt())
        );
    }

    @DeleteMapping("/{provider}")
    public ResponseEntity<Void> deleteAiConfig(
            @PathVariable String provider,
            @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        DeleteAiConfigCommand command = new DeleteAiConfigCommand(userId, provider);
        deleteAiConfig.execute(command);
        return ResponseEntity.noContent().build();
    }
}
