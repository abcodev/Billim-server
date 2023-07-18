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

	@Schema(description = "채팅 내용 고유번호")
	private long messageId;

	@Schema(description = "채팅 보낸 회원 고유번호")
	private long senderId;

	@Schema(description = "채팅 메시지 타입")
	private ChatMessageType type;

	@Schema(description = "채팅 메시지")
	private String message;

	@Schema(description = "채팅 읽음 여부")
	private boolean isRead;

	@Schema(description = "채팅 보낸 날짜")
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
