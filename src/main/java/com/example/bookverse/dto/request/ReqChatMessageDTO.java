package com.example.bookverse.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReqChatMessageDTO(
    @NotBlank(message = "sessionId không được để trống") 
    String sessionId,
    
    @NotBlank(message = "message không được để trống") 
    String message) {
}