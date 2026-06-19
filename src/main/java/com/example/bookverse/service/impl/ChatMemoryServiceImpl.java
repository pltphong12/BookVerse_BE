package com.example.bookverse.service.impl;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.bookverse.dto.record.ChatMemoryMessage;
import com.example.bookverse.service.ChatMemoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatMemoryServiceImpl implements ChatMemoryService {
    private static final String KEY_PREFIX = "chat:memory:";
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final int maxMessages;
    private final Duration ttl;

    public ChatMemoryServiceImpl(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            @Value("${bookverse.chatbot.max-memory-messages:5}") int maxMessages,
            @Value("${bookverse.chatbot.memory-ttl-minutes:60}") long ttlMinutes) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.maxMessages = maxMessages;
        this.ttl = Duration.ofMinutes(ttlMinutes);
    }

    @Override
    public List<ChatMemoryMessage> getRecentMessages(String sessionId) {
        List<String> values = redisTemplate.opsForList().range(buildKey(sessionId), 0, -1);
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return values.stream()
                .map(this::deserialize)
                .toList();
    }

    @Override
    public void addUserMessage(String sessionId, String message) {
        addMessage(sessionId, ChatMemoryMessage.Role.USER, message);
    }

    @Override
    public void addAssistantMessage(String sessionId, String message) {
        addMessage(sessionId, ChatMemoryMessage.Role.ASSISTANT, message);
    }

    private void addMessage(String sessionId, ChatMemoryMessage.Role role, String content) {
        if (content == null || content.isBlank()) {
            return;
        }
        String key = buildKey(sessionId);
        ChatMemoryMessage memoryMessage = new ChatMemoryMessage(role, content.trim());
        redisTemplate.opsForList().rightPush(key, serialize(memoryMessage));
        redisTemplate.opsForList().trim(key, -maxMessages, -1);
        redisTemplate.expire(key, ttl);
    }

    private String buildKey(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return KEY_PREFIX + "anonymous";
        }
        return KEY_PREFIX + sessionId.trim();
    }

    private String serialize(ChatMemoryMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Không thể serialize chat memory", e);
        }
    }

    private ChatMemoryMessage deserialize(String value) {
        try {
            return objectMapper.readValue(value, ChatMemoryMessage.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Không thể deserialize chat memory", e);
        }
    }
}
