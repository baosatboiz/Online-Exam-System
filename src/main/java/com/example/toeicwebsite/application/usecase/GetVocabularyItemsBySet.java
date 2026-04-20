package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.query.GetVocabularyItemsBySetQuery;
import com.example.toeicwebsite.application.result.VocabularyItemResult;

import java.util.List;

public interface GetVocabularyItemsBySet {
    List<VocabularyItemResult> handle(GetVocabularyItemsBySetQuery query);
}
