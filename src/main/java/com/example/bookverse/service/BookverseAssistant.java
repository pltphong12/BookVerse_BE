package com.example.bookverse.service;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface BookverseAssistant {
    @UserMessage("{{message}}")
    TokenStream chat(String message);
}
