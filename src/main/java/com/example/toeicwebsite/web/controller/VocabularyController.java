package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.AddVocabularyItemCommand;
import com.example.toeicwebsite.application.command.CreateVocabularySetCommand;
import com.example.toeicwebsite.application.command.CreateVocabularySetWithItemsCommand;
import com.example.toeicwebsite.application.command.DeleteVocabularySetCommand;
import com.example.toeicwebsite.application.command.UpdateVocabularySetWithItemsCommand;
import com.example.toeicwebsite.application.query.GetVocabularyItemsBySetQuery;
import com.example.toeicwebsite.application.query.GetVocabularySetsQuery;
import com.example.toeicwebsite.application.result.CreateVocabularySetWithItemsResult;
import com.example.toeicwebsite.application.result.VocabularyItemResult;
import com.example.toeicwebsite.application.result.VocabularySetResult;
import com.example.toeicwebsite.application.usecase.AddVocabularyItem;
import com.example.toeicwebsite.application.usecase.CreateVocabularySet;
import com.example.toeicwebsite.application.usecase.CreateVocabularySetWithItems;
import com.example.toeicwebsite.application.usecase.DeleteVocabularySet;
import com.example.toeicwebsite.application.usecase.GetVocabularyItemsBySet;
import com.example.toeicwebsite.application.usecase.GetVocabularySets;
import com.example.toeicwebsite.application.usecase.UpdateVocabularySetWithItems;
import com.example.toeicwebsite.domain.user.model.UserId;
import com.example.toeicwebsite.domain.vocabulary.model.VocabularySetId;
import com.example.toeicwebsite.infrastucture.security.config.SecurityUser;
import com.example.toeicwebsite.web.dto.vocabulary.request.AddVocabularyItemRequest;
import com.example.toeicwebsite.web.dto.vocabulary.request.CreateVocabularySetRequest;
import com.example.toeicwebsite.web.dto.vocabulary.request.CreateVocabularySetWithItemsRequest;
import com.example.toeicwebsite.web.dto.vocabulary.request.UpdateVocabularySetWithItemsRequest;
import com.example.toeicwebsite.web.dto.vocabulary.response.CreateVocabularySetWithItemsResponse;
import com.example.toeicwebsite.web.dto.vocabulary.response.VocabularyItemResponse;
import com.example.toeicwebsite.web.dto.vocabulary.response.VocabularySetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vocabulary")
@RequiredArgsConstructor
public class VocabularyController {
    private final CreateVocabularySet createVocabularySet;
    private final AddVocabularyItem addVocabularyItem;
    private final CreateVocabularySetWithItems createVocabularySetWithItems;
    private final GetVocabularySets getVocabularySets;
    private final GetVocabularyItemsBySet getVocabularyItemsBySet;
    private final UpdateVocabularySetWithItems updateVocabularySetWithItems;
    private final DeleteVocabularySet deleteVocabularySet;

    @GetMapping("/sets")
    public ResponseEntity<List<VocabularySetResponse>> getSets(@AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        List<VocabularySetResult> sets = getVocabularySets.handle(new GetVocabularySetsQuery(userId));
        List<VocabularySetResponse> response = sets.stream()
                .map(set -> new VocabularySetResponse(
                        set.setId().value(),
                        set.name(),
                        set.description(),
                        set.createdAt(),
                        set.updatedAt(),
                        set.itemCount()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sets")
    public ResponseEntity<VocabularySetResponse> createSet(@RequestBody @Valid CreateVocabularySetRequest request,
                                                           @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        VocabularySetResult set = createVocabularySet.execute(new CreateVocabularySetCommand(userId, request.name(), request.description()));
        return ResponseEntity.ok(new VocabularySetResponse(
                set.setId().value(),
                set.name(),
                set.description(),
                set.createdAt(),
                set.updatedAt(),
                set.itemCount()
        ));
    }

    @PostMapping("/sets/with-items")
    public ResponseEntity<CreateVocabularySetWithItemsResponse> createSetWithItems(
            @RequestBody @Valid CreateVocabularySetWithItemsRequest request,
            @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        List<CreateVocabularySetWithItemsCommand.ItemInput> itemInputs = request.items() == null
                ? List.of()
                : request.items().stream()
                .map(item -> new CreateVocabularySetWithItemsCommand.ItemInput(
                        item.term(),
                        item.meaning(),
                        item.note(),
                        item.example()
                )).toList();

        CreateVocabularySetWithItemsResult result = createVocabularySetWithItems.execute(
                new CreateVocabularySetWithItemsCommand(
                        userId,
                        request.name(),
                        request.description(),
                        itemInputs
                )
        );

        return ResponseEntity.ok(new CreateVocabularySetWithItemsResponse(
                result.setId().value(),
                result.createdItemsCount(),
                result.skippedTerms()
        ));
    }

    @GetMapping("/sets/{setId}/items")
    public ResponseEntity<List<VocabularyItemResponse>> getItemsBySet(@PathVariable UUID setId,
                                                                      @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        List<VocabularyItemResult> items = getVocabularyItemsBySet.handle(
                new GetVocabularyItemsBySetQuery(userId, new VocabularySetId(setId))
        );

        List<VocabularyItemResponse> response = items.stream().map(item -> new VocabularyItemResponse(
                item.itemId().value(),
                item.term(),
                item.meaning(),
                item.note(),
                item.example(),
                item.createdAt(),
                item.updatedAt()
        )).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sets/{setId}/items")
    public ResponseEntity<VocabularyItemResponse> addItem(@PathVariable UUID setId,
                                                          @RequestBody @Valid AddVocabularyItemRequest request,
                                                          @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        VocabularyItemResult item = addVocabularyItem.execute(new AddVocabularyItemCommand(
                userId,
                new VocabularySetId(setId),
                request.term(),
                request.meaning(),
                request.note(),
                request.example()
        ));
        return ResponseEntity.ok(new VocabularyItemResponse(
                item.itemId().value(),
                item.term(),
                item.meaning(),
                item.note(),
                item.example(),
                item.createdAt(),
                item.updatedAt()
        ));
    }

    @PutMapping("/sets/{setId}/with-items")
    public ResponseEntity<VocabularySetResponse> updateSetWithItems(@PathVariable UUID setId,
                                                                    @RequestBody @Valid UpdateVocabularySetWithItemsRequest request,
                                                                    @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        List<UpdateVocabularySetWithItemsCommand.ItemUpdate> items = request.items() == null
                ? List.of()
                : request.items().stream()
                .map(item -> new UpdateVocabularySetWithItemsCommand.ItemUpdate(
                        item.itemId(),
                        item.term(),
                        item.meaning(),
                        item.note(),
                        item.example(),
                        item.isNew()
                )).toList();

        VocabularySetResult set = updateVocabularySetWithItems.execute(
                new UpdateVocabularySetWithItemsCommand(
                        userId,
                        new VocabularySetId(setId),
                        request.name(),
                        request.description(),
                        items
                )
        );
        return ResponseEntity.ok(new VocabularySetResponse(
                set.setId().value(),
                set.name(),
                set.description(),
                set.createdAt(),
                set.updatedAt(),
                set.itemCount()
        ));
    }

    @DeleteMapping("/sets/{setId}")
    public ResponseEntity<Void> deleteSet(@PathVariable UUID setId,
                                         @AuthenticationPrincipal SecurityUser securityUser) {
        UserId userId = securityUser.getUser().getUserId();
        deleteVocabularySet.execute(new DeleteVocabularySetCommand(userId, new VocabularySetId(setId)));
        return ResponseEntity.noContent().build();
    }
}
