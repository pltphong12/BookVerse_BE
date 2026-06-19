package com.example.bookverse.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.bookverse.dto.record.ChatHistoryResponse;
import com.example.bookverse.dto.request.ReqChatMessageDTO;
import com.example.bookverse.service.ChatbotService;

import dev.langchain4j.service.TokenStream;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/chat")
@Validated
public class ChatController {
    private final ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/history")
    public ResponseEntity<ChatHistoryResponse> getHistory(
            @RequestParam @NotBlank(message = "sessionId không được để trống") String sessionId) {
        ChatHistoryResponse history = chatbotService.getHistory(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> stream(@Valid @RequestBody ReqChatMessageDTO req) {
        SseEmitter emitter = new SseEmitter(120_000L);
        StringBuffer assistantResponse = new StringBuffer();
        TokenStream tokenStream = chatbotService.stream(req.sessionId(), req.message());
        tokenStream
                .onPartialResponse(token -> {
                    assistantResponse.append(token);
                    sendToken(emitter, token);
                })
                .onCompleteResponse(response -> {
                    chatbotService.rememberAssistantMessage(req.sessionId(), assistantResponse.toString());
                    emitter.complete();
                })
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
