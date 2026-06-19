package com.example.bookverse.service;

import com.example.bookverse.dto.record.ChatHistoryResponse;

import dev.langchain4j.service.TokenStream;

public interface ChatbotService {
    TokenStream stream(String sessionId, String message);

    void rememberAssistantMessage(String sessionId, String message);

    ChatHistoryResponse getHistory(String sessionId);
}
