package com.example.bookverse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.bookverse.dto.record.ChatHistoryResponse;
import com.example.bookverse.dto.record.ChatMemoryMessage;
import com.example.bookverse.dto.record.RagSearchResult;
import com.example.bookverse.service.BookverseAssistant;
import com.example.bookverse.service.ChatMemoryService;
import com.example.bookverse.service.ChatbotService;
import com.example.bookverse.service.RagRetrievalService;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

@Service
public class ChatbotServiceImpl implements ChatbotService {
    private static final String OUT_OF_SCOPE_RESPONSE = "Hiện tại mình chưa có đủ thông tin về nội dung này trong dữ liệu BookVerse.";
    private final RagRetrievalService ragRetrievalService;
    private final ChatMemoryService chatMemoryService;
    private final BookverseAssistant assistant;
    private final int topK;

    public ChatbotServiceImpl(
            RagRetrievalService ragRetrievalService,
            StreamingChatModel streamingChatModel,
            ChatMemoryService chatMemoryService,
            @Value("${bookverse.chatbot.top-k:3}") int topK) {
        this.ragRetrievalService = ragRetrievalService;
        this.chatMemoryService = chatMemoryService;
        this.assistant = AiServices.builder(BookverseAssistant.class)
                .streamingChatModel(streamingChatModel)
                .build();
        this.topK = topK;
    }

    @Override
    public TokenStream stream(String sessionId, String message) {
        List<ChatMemoryMessage> history = chatMemoryService.getRecentMessages(sessionId);
        String retrievalQuery = buildRetrievalQuery(message, history);
        List<RagSearchResult> contexts = ragRetrievalService.retrieve(retrievalQuery, topK);
        String prompt = buildPrompt(sessionId, message, history, contexts);

        chatMemoryService.addUserMessage(sessionId, message);

        return assistant.chat(prompt);
    }

    @Override
    public void rememberAssistantMessage(String sessionId, String message) {
        chatMemoryService.addAssistantMessage(sessionId, message);
    }

    @Override
    public ChatHistoryResponse getHistory(String sessionId) {
        return new ChatHistoryResponse(sessionId, chatMemoryService.getRecentMessages(sessionId));
    }

    private String buildPrompt(
            String sessionId,
            String message,
            List<ChatMemoryMessage> history,
            List<RagSearchResult> contexts) {
        return """
                Bạn là trợ lý bán sách của BookVerse.

                QUY TẮC BẮT BUỘC:
                - Chỉ trả lời dựa trên CONTEXT được cung cấp bên dưới.
                - Không tự bịa giá bán, tồn kho, tác giả, nhà xuất bản, khuyến mãi hoặc thông tin sản phẩm.
                - Nếu CONTEXT không có thông tin phù hợp, hãy trả lời đúng câu sau:
                  "%s"
                - Từ chối khéo các câu hỏi ngoài phạm vi tư vấn sách/sản phẩm BookVerse.
                - Trả lời bằng tiếng Việt, thân thiện, ngắn gọn.

                NHẤT QUÁN VỚI LỊCH SỬ HỘI THOẠI:
                - Sách "đã gợi ý" chỉ là những cuốn trợ lý đã nêu tên trong LỊCH SỬ HỘI THOẠI, không phải mọi cuốn trong CONTEXT.
                - Nếu khách hỏi tiếp về giá, tồn kho, chi tiết hoặc lọc theo điều kiện (ví dụ tầm giá) của sách vừa gợi ý:
                  chỉ trả lời những cuốn đã nêu tên trong LỊCH SỬ.
                - Cuốn có trong CONTEXT nhưng chưa từng được nêu trong LỊCH SỬ thì không được nói là "đã gợi ý".
                  Chỉ được giới thiệu thêm nếu khách đang tìm sách mới, và phải nói rõ đây là gợi ý thêm.
                - Không viết "cả N cuốn đã gợi ý" trừ khi N đúng bằng số sách trợ lý đã nêu tên ở lượt trước.
                - Số lượng sách trong CONTEXT không được dùng để suy ra số sách đã gợi ý.

                SESSION_ID:
                %s

                LỊCH SỬ HỘI THOẠI GẦN NHẤT:
                %s

                CONTEXT:
                %s

                CÂU HỎI KHÁCH HÀNG:
                %s
                """.formatted(
                OUT_OF_SCOPE_RESPONSE,
                sessionId,
                formatHistory(history),
                formatContexts(contexts),
                message);
    }

    private String buildRetrievalQuery(String message, List<ChatMemoryMessage> history) {
        if (history == null || history.isEmpty()) {
            return message;
        }
    
        return """
                Lịch sử hội thoại gần nhất:
                %s
    
                Câu hỏi hiện tại: %s
                """.formatted(formatHistory(history), message);
    }
    
    private String formatHistory(List<ChatMemoryMessage> history) {
        if (history == null || history.isEmpty()) {
            return "Không có lịch sử hội thoại.";
        }
    
        StringBuilder builder = new StringBuilder();
        for (ChatMemoryMessage item : history) {
            builder.append(item.role() == ChatMemoryMessage.Role.USER ? "Khách hàng: " : "Trợ lý: ")
                    .append(item.content())
                    .append('\n');
        }
        return builder.toString().trim();
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
