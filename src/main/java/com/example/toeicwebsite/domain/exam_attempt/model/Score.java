package com.example.toeicwebsite.domain.exam_attempt.model;

import lombok.Getter;

@Getter
public class Score {
    private int listeningCorrect;
    private int readingCorrect;
    private int listeningWrong = 0;
    private int readingWrong = 0;
    private int listeningUnanswered = 0;
    private int readingUnanswered = 0;

    public Score(int listeningCorrect, int readingCorrect) {
        this.listeningCorrect = listeningCorrect;
        this.readingCorrect = readingCorrect;
    }

    public Score(
            int listeningCorrect,
            int readingCorrect,
            int listeningWrong,
            int readingWrong,
            int listeningUnanswered,
            int readingUnanswered
    ) {
        this.listeningCorrect = listeningCorrect;
        this.readingCorrect = readingCorrect;
        this.listeningWrong = listeningWrong;
        this.readingWrong = readingWrong;
        this.listeningUnanswered = listeningUnanswered;
        this.readingUnanswered = readingUnanswered;
    }

    public int totalCorrect() {
        return listeningCorrect + readingCorrect;
    }

    public int totalWrong() {
        return listeningWrong + readingWrong;
    }

    public int totalUnanswered() {
        return listeningUnanswered + readingUnanswered;
    }
}
