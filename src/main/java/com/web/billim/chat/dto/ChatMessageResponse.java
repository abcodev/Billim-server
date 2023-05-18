package com.web.billim.chat.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.type.ChatMessageType;

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
	private String imageUrl;
	private boolean isRead;
	private LocalDateTime sendAt;

	public static ChatMessageResponse from(ChatMessage chatMessage) {
		return ChatMessageResponse.builder()
			.messageId(chatMessage.getId())
			.senderId(chatMessage.getSender().getMemberId())
			.type(chatMessage.getType())
			.message(chatMessage.getMessage())
			.imageUrl(chatMessage.getImageUrl())
			.isRead(chatMessage.isRead())
			.sendAt(chatMessage.getCreatedAt())
			.build();
	}
}

/**
 *  NULL 인 필드는 JSON 필드로 포함하지 말아라
 *   {
 *		imageUrl: null
 *   }
 *   {
 *
 *   }
 */
