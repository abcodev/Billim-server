package com.web.billim.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.type.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageResponse {

    private boolean isNewMessage; // true 면 기존대로 밑에 추가, false 면 기존 값(messageId 기준)을 찾아서 변경
    private long chatRoomId;
    private long messageId;
    private String senderNickname;
    private long senderId;
    private ChatMessageType type;
    private String message;
    private boolean isRead;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime sendAt;

    public static ChatMessageResponse createNewMessage(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .isNewMessage(true)
                .chatRoomId(chatMessage.getChatRoom().getId())
                .messageId(chatMessage.getId())
                .senderNickname(chatMessage.getType() != ChatMessageType.SYSTEM ? chatMessage.getSender().getNickname() : "SYSTEM")
                .senderId(chatMessage.getType() != ChatMessageType.SYSTEM ? chatMessage.getSender().getMemberId() : -1)
                .type(chatMessage.getType())
                .message(chatMessage.getMessage())
                .isRead(chatMessage.isRead())
                .sendAt(chatMessage.getCreatedAt())
                .build();
    }

    public static ChatMessageResponse updatedMessage(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .isNewMessage(false)
                .messageId(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderNickname(chatMessage.getType() != ChatMessageType.SYSTEM ? chatMessage.getSender().getNickname() : "SYSTEM")
                .senderId(chatMessage.getType() != ChatMessageType.SYSTEM ? chatMessage.getSender().getMemberId() : -1)
                .type(chatMessage.getType())
                .message(chatMessage.getMessage())
                .isRead(chatMessage.isRead())
                .sendAt(chatMessage.getCreatedAt())
                .build();
    }

}
