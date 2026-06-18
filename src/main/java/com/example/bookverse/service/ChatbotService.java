package com.example.bookverse.service;

import dev.langchain4j.service.TokenStream;

public interface ChatbotService {
    TokenStream stream(String sessionId, String message);
}
