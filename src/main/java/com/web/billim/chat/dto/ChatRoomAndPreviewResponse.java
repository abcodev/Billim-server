package com.web.billim.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.domain.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomAndPreviewResponse {

	private long chatRoomId;
	private long opponentId;
	private String opponentNickname;
	private String opponentProfileImageUrl;
	private int unreadCount;
	private String latestMessage;
	private LocalDateTime latestMessageTime;

	// ChatRoomAndPreviewResponse 를 ResponseEntity 에 담아서 응답을 보냈음
	// 그런데 지금 응답으로 받아본건 JSON 포맷의 데이터.
	// 이 매핑작업을 해주는게 Spring. -> Jackson Library
	public static ChatRoomAndPreviewResponse of(ChatRoom chatRoom, ChatMessage latestMessage, int unreadCount) {
		ChatRoomAndPreviewResponseBuilder builder = ChatRoomAndPreviewResponse.builder()
			.chatRoomId(chatRoom.getId())
			.opponentId(chatRoom.getBuyer().getMemberId())
			.opponentNickname(chatRoom.getBuyer().getNickname())
			.opponentProfileImageUrl(chatRoom.getBuyer().getProfileImageUrl())
			.unreadCount(unreadCount);

		if (latestMessage != null) {
			builder = builder.latestMessageTime(latestMessage.getCreatedAt())
				.latestMessage(latestMessage.getMessage());
		}
		return builder.build();
	}
}

/*
	판매자가 내가 올린 Product 1 을 누른다.
    Product 1 을 사려고 메시지를 보낸 사람들이 있음
    그 채팅방 목록
 */