package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.GetVocabularySetsQuery;
import com.example.toeicwebsite.application.result.VocabularySetResult;

import java.util.List;

public interface GetVocabularySets {
    List<VocabularySetResult> handle(GetVocabularySetsQuery query);
}
