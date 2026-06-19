package com.example.bookverse.dto.record;

import java.util.List;

public record ChatHistoryResponse(
        String sessionId,
        List<ChatMemoryMessage> messages
) {
}
