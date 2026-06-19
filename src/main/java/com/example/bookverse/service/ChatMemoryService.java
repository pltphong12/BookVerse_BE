package com.example.bookverse.service;

import java.util.List;

import com.example.bookverse.dto.record.ChatMemoryMessage;

public interface ChatMemoryService {
    List<ChatMemoryMessage> getRecentMessages(String sessionId);

    void addUserMessage(String sessionId, String message);

    void addAssistantMessage(String sessionId, String message);
}
