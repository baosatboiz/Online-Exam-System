package com.example.toeicwebsite.domain.user.model;

import java.util.UUID;


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