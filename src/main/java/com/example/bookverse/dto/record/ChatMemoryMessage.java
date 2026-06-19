package com.example.bookverse.dto.record;

public record ChatMemoryMessage(Role role, String content) {
    public enum Role {
        USER,
        ASSISTANT
    }
}
