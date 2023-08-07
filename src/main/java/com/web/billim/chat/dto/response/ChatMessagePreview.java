package com.web.billim.chat.dto.response;

import java.time.LocalDateTime;

import com.web.billim.chat.domain.ChatMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagePreview {

	@Schema(description = "읽지 않은 메시지 개수")
	private int unreadCount;

	@Schema(description = "최신 채팅 메시지")
	private String latestMessage;

	@Schema(description = "최신 채팅 시간")
	private LocalDateTime latestMessageTime;

	public static ChatMessagePreview of(ChatMessage latestMessage, int unreadCount) {
		return new ChatMessagePreview(unreadCount, latestMessage.getMessage(), latestMessage.getCreatedAt());
	}

	public static ChatMessagePreview empty() {
		return new ChatMessagePreview(0, "", LocalDateTime.now());
	}

}
