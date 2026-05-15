package com.example.toeicwebsite.domain.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserId {
    private UUID id;

    public UserId(UUID id) {
        this.id = id;
    }
    public UUID value() {
        return id;
    }
    public static UserId newId(){ return new UserId(UUID.randomUUID()); }
    @Override
    public boolean equals(Object o) {
        return o instanceof UserId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}