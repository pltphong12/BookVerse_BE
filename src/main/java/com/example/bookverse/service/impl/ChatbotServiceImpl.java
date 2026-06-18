package com.example.bookverse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.bookverse.dto.response.RagSearchResult;
import com.example.bookverse.service.BookverseAssistant;
import com.example.bookverse.service.ChatbotService;
import com.example.bookverse.service.RagRetrievalService;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

@Service
public class ChatbotServiceImpl implements ChatbotService{
    private static final String OUT_OF_SCOPE_RESPONSE =
            "Hiện tại mình chưa có đủ thông tin về nội dung này trong dữ liệu BookVerse.";
    private final RagRetrievalService ragRetrievalService;
    private final BookverseAssistant assistant;
    private final int topK;
    public ChatbotServiceImpl(
            RagRetrievalService ragRetrievalService,
            StreamingChatModel streamingChatModel,
            @Value("${bookverse.chatbot.top-k:3}") int topK) {
        this.ragRetrievalService = ragRetrievalService;
        this.assistant = AiServices.builder(BookverseAssistant.class)
                .streamingChatModel(streamingChatModel)
                .build();
        this.topK = topK;
    }
    @Override
    public TokenStream stream(String sessionId, String message) {
        List<RagSearchResult> contexts = ragRetrievalService.retrieve(message, topK);
        String prompt = buildPrompt(sessionId, message, contexts);
        return assistant.chat(prompt);
    }
    private String buildPrompt(String sessionId, String message, List<RagSearchResult> contexts) {
        return """
                Bạn là trợ lý bán sách của BookVerse.
                QUY TẮC BẮT BUỘC:
                - Chỉ trả lời dựa trên CONTEXT được cung cấp bên dưới.
                - Không tự bịa giá bán, tồn kho, tác giả, nhà xuất bản, khuyến mãi hoặc thông tin sản phẩm.
                - Nếu CONTEXT không có thông tin phù hợp, hãy trả lời đúng câu sau:
                  "%s"
                - Từ chối khéo các câu hỏi ngoài phạm vi tư vấn sách/sản phẩm BookVerse.
                - Trả lời bằng tiếng Việt, thân thiện, ngắn gọn.
                SESSION_ID:
                %s
                CONTEXT:
                %s
                CÂU HỎI KHÁCH HÀNG:
                %s
                """.formatted(
                OUT_OF_SCOPE_RESPONSE,
                sessionId,
                formatContexts(contexts),
                message);
    }
    private String formatContexts(List<RagSearchResult> contexts) {
        if (contexts == null || contexts.isEmpty()) {
            return "Không tìm thấy context phù hợp.";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < contexts.size(); i++) {
            RagSearchResult context = contexts.get(i);
            builder.append("[")
                    .append(i + 1)
                    .append("] ")
                    .append(context.title())
                    .append(" | score=")
                    .append(context.score())
                    .append('\n')
                    .append(context.ragContent())
                    .append("\n\n");
        }
        return builder.toString().trim();
    }  
}
