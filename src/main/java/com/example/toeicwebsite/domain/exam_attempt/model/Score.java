package com.example.toeicwebsite.domain.exam_attempt.model;

public class Score {
    private int listening;
    private int reading;
    public Score(int listening, int reading) {
        this.listening =  listening;
        this.reading = reading;
    }
    public int getTotalCorrect(){ return listening+reading;}
}
