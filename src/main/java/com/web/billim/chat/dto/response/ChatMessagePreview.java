package com.web.billim.chat.dto.response;

import java.time.LocalDateTime;

import com.web.billim.chat.domain.ChatMessage;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagePreview {

	@ApiModelProperty(value = "읽지 않은 메세지 개수")
	private int unreadCount;

	@ApiModelProperty(value = "최신 채팅")
	private String latestMessage;

	@ApiModelProperty(value = "최신 채팅 시간")
	private LocalDateTime latestMessageTime;

	public static ChatMessagePreview of(ChatMessage latestMessage, int unreadCount) {
		return new ChatMessagePreview(unreadCount, latestMessage.getMessage(), latestMessage.getCreatedAt());
	}

}
