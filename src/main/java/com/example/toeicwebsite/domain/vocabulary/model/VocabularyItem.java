package com.example.toeicwebsite.domain.vocabulary.model;

import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.Getter;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

@Getter
public class VocabularyItem {
    private final VocabularyItemId vocabularyItemId;
    private final VocabularySetId vocabularySetId;
    private final UserId userId;
    private final String term;
    private final String normalizedTerm;
    private final String meaning;
    private final String note;
    private final String example;
    private final String pronunciation;
    private final String audioUrl;
    private final Instant createdAt;
    private final Instant updatedAt;

    public VocabularyItem(VocabularyItemId vocabularyItemId, VocabularySetId vocabularySetId, UserId userId, 
                          String term, String normalizedTerm, String meaning, String note, String example,
                          String pronunciation, String audioUrl, Instant createdAt, Instant updatedAt) {
        this.vocabularyItemId = Objects.requireNonNull(vocabularyItemId);
        this.vocabularySetId = Objects.requireNonNull(vocabularySetId);
        this.userId = Objects.requireNonNull(userId);
        this.term = Objects.requireNonNull(term).trim();
        this.normalizedTerm = Objects.requireNonNull(normalizedTerm);
        this.meaning = Objects.requireNonNull(meaning).trim();
        this.note = note == null ? null : note.trim();
        this.example = example == null ? null : example.trim();
        this.pronunciation = pronunciation == null ? null : pronunciation.trim();
        this.audioUrl = audioUrl == null ? null : audioUrl.trim();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static VocabularyItem create(VocabularySetId setId,UserId userId,String term,String meaning,String note,String example) {
        return new VocabularyItem(VocabularyItemId.newId(),setId,userId,term,normalizeTerm(term),meaning,note,example,null,null,null,null);
    }

    public static VocabularyItem createWithPronunciation(VocabularySetId setId, UserId userId, String term, String meaning, 
                                                         String note, String example, String pronunciation, String audioUrl) {
        return new VocabularyItem(VocabularyItemId.newId(),setId,userId,term,normalizeTerm(term),meaning,note,example,
                                  pronunciation, audioUrl, null, null);
    }

    public VocabularyItem update(String term, String meaning, String note, String example) {
        return new VocabularyItem(this.vocabularyItemId, this.vocabularySetId, this.userId, 
                                  term, normalizeTerm(term), meaning, note, example, 
                                  this.pronunciation, this.audioUrl, this.createdAt, Instant.now());
    }

    public VocabularyItem updateWithPronunciation(String term, String meaning, String note, String example, 
                                                  String pronunciation, String audioUrl) {
        return new VocabularyItem(this.vocabularyItemId, this.vocabularySetId, this.userId, 
                                  term, normalizeTerm(term), meaning, note, example, 
                                  pronunciation, audioUrl, this.createdAt, Instant.now());
    }

    public static String normalizeTerm(String term) {
        return term == null ? "" : term.trim().toLowerCase(Locale.ROOT);
    }
}
