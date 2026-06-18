package com.example.bookverse.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.bookverse.dto.request.ReqChatMessageDTO;
import com.example.bookverse.service.ChatbotService;

import dev.langchain4j.service.TokenStream;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> stream(@Valid @RequestBody ReqChatMessageDTO req) {
        SseEmitter emitter = new SseEmitter(120_000L);
        TokenStream tokenStream = chatbotService.stream(req.sessionId(), req.message());
        tokenStream
                .onPartialResponse(token -> sendToken(emitter, token))
                .onCompleteResponse(response -> emitter.complete())
                .onError(error -> emitter.completeWithError(error))
                .start();
        return ResponseEntity.ok(emitter);
    }

    private void sendToken(SseEmitter emitter, String token) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("token")
                            .data(token));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }
}
