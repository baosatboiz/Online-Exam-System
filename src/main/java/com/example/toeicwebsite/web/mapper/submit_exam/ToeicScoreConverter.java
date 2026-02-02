package com.example.toeicwebsite.web.mapper.submit_exam;

import java.util.HashMap;
import java.util.Map;

public class ToeicScoreConverter {
    private static final Map<Integer, Integer> listeningScoreMap = new HashMap<>();
    private static final Map<Integer, Integer> readingScoreMap = new HashMap<>();

    static {
        listeningScoreMap.put(0, 5);
        for (int i = 1; i <= 75; i++) {
            listeningScoreMap.put(i, i * 5 + 10);
        }
        for(int i = 76; i <= 100; i++) {
            listeningScoreMap.put(i, Math.min(i * 5 + 15, 495));
        }
        readingScoreMap.put(0, 5);
        readingScoreMap.put(1, 5);
        for (int i = 2; i <= 100; i++) {
            readingScoreMap.put(i, i * 5 - 5);
        }
    }

    public static Integer listeningScore(Integer correctAnswers) {
        return listeningScoreMap.getOrDefault(correctAnswers, 0);
    }

    public static Integer readingScore(Integer correctAnswers) {
        return readingScoreMap.getOrDefault(correctAnswers, 0);
    }
}
