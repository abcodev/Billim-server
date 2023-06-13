package com.web.billim.chat.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.type.ChatMessageType;

import io.swagger.annotations.ApiModelProperty;
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

	@ApiModelProperty(value = "채팅 내용 고유번호")
	private long messageId;
	@ApiModelProperty(value = "채팅 보낸 회원 고유번호")
	private long senderId;

	@ApiModelProperty(value = "채팅 메세지 타입 : TEXT, IMAGE")
	private ChatMessageType type;

	@ApiModelProperty(value = "채팅 내용")
	private String message;

	@ApiModelProperty(value = "채팅 사진 이미지 주소")
	private String imageUrl;

	@ApiModelProperty(value = "채팅 읽음 여부")
	private boolean isRead;

	@ApiModelProperty(value = "채팅 보낸 날짜")
	private LocalDateTime sendAt;

	public static ChatMessageResponse from(ChatMessage chatMessage) {
		return ChatMessageResponse.builder()
			.messageId(chatMessage.getId())
			.senderId(chatMessage.getType() != ChatMessageType.SYSTEM ? chatMessage.getSender().getMemberId() : -1)
			.type(chatMessage.getType())
			.message(chatMessage.getMessage())
			.imageUrl(chatMessage.getImageUrl())
			.isRead(chatMessage.isRead())
			.sendAt(chatMessage.getCreatedAt())
			.build();
	}
}

/*
	NULL 인 필드는 JSON 필드로 포함하지 말아라
	{
		imageUrl: null
	}
	{

	}
*/
