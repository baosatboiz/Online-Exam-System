package com.example.toeicwebsite.domain.exam.model;

public enum PartType {
    PART_1_LISTENING,
    PART_2_LISTENING,
    PART_3_LISTENING,
    PART_4_LISTENING,
    PART_5_READING,
    PART_6_READING,
    PART_7_READING;
    public boolean isListening(){
        return this.ordinal() <= PartType.PART_4_LISTENING.ordinal();
    }
}
