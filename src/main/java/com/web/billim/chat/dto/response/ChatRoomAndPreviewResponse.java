package com.web.billim.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.chat.domain.ChatRoom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomAndPreviewResponse {

	@Schema(description = "채팅방 고유번호")
	private long chatRoomId;

	@Schema(description = "채팅 상대방 회원 고유번호")
	private long receiverId;

	@Schema(description = "채팅 상대방 회원 닉네임")
	private String receiverNickname;

	@Schema(description = "채팅 상대방 회원 프로필 이미지 주소")
	private String receiverProfileImageUrl;

	@Schema(description = "읽지 않은 메시지 개수")
	private int unreadCount;

	@Schema(description = "최신 채팅 메시지")
	private String latestMessage;

	@Schema(description = "최신 채팅 시간")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime latestMessageTime;

	public static ChatRoomAndPreviewResponse forSeller(ChatRoom chatRoom, ChatMessagePreview preview) {
		ChatRoomAndPreviewResponseBuilder builder = ChatRoomAndPreviewResponse.builder()
			.chatRoomId(chatRoom.getId())
			.receiverId(chatRoom.getBuyer().getMemberId())
			.receiverNickname(chatRoom.getBuyer().getNickname())
			.receiverProfileImageUrl(chatRoom.getBuyer().getProfileImageUrl())
			.unreadCount(preview.getUnreadCount());

		if (preview.getLatestMessage() != null) {
			builder = builder.latestMessageTime(preview.getLatestMessageTime())
				.latestMessage(preview.getLatestMessage());
		}
		return builder.build();
	}

	public static ChatRoomAndPreviewResponse forBuyer(ChatRoom chatRoom, ChatMessagePreview preview) {
		ChatRoomAndPreviewResponseBuilder builder = ChatRoomAndPreviewResponse.builder()
			.chatRoomId(chatRoom.getId())
			.receiverId(chatRoom.getSeller().getMemberId())
			.receiverNickname(chatRoom.getSeller().getNickname())
			.receiverProfileImageUrl(chatRoom.getSeller().getProfileImageUrl())
			.unreadCount(preview.getUnreadCount());

		if (preview.getLatestMessage() != null) {
			builder = builder.latestMessageTime(preview.getLatestMessageTime())
				.latestMessage(preview.getLatestMessage());
		}
		return builder.build();
	}

}

/*
	판매자가 내가 올린 Product 1 을 누른다.
    Product 1 을 사려고 메시지를 보낸 사람들이 있음
    그 채팅방 목록
 */