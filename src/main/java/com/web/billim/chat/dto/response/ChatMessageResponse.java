package com.web.billim.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.type.ChatMessageType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageResponse {

    private long messageId;
    private long senderId;
    private ChatMessageType type;
    private String message;
    private boolean isRead;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime sendAt;

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .messageId(chatMessage.getId())
                .senderId(chatMessage.getType() != ChatMessageType.SYSTEM ? chatMessage.getSender().getMemberId() : -1)
                .type(chatMessage.getType())
                .message(chatMessage.getMessage())
                .isRead(chatMessage.isRead())
                .sendAt(chatMessage.getCreatedAt())
                .build();
    }
}
