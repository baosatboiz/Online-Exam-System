package com.example.toeicwebsite.domain.exam.model;

import lombok.Getter;

@Getter
public enum PartType {
    PART_1_LISTENING(1),
    PART_2_LISTENING(2),
    PART_3_LISTENING(3),
    PART_4_LISTENING(4),
    PART_5_READING(5),
    PART_6_READING(6),
    PART_7_READING(7);
    private final int code;

    PartType(int code) {
        this.code = code;
    }

    public static PartType fromCode(Integer code) {
        if (code == null) return null;
        for (PartType p : values()) {
            if (p.code == code) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown PartType code: " + code);
    }
    public boolean isListening(){
        return this.ordinal() <= PartType.PART_4_LISTENING.ordinal();
    }
}
