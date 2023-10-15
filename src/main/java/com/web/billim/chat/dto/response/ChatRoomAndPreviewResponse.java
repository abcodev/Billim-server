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

	private long chatRoomId;
	private long receiverId;
	private String receiverNickname;
	private String receiverProfileImageUrl;
	private int unreadCount;
	private String latestMessage;
	private boolean isDisable; // 이게 TRUE 면 입력창 막기

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime latestMessageTime;

	public static ChatRoomAndPreviewResponse forSeller(ChatRoom chatRoom, ChatMessagePreview preview) {
		ChatRoomAndPreviewResponseBuilder builder = ChatRoomAndPreviewResponse.builder()
			.chatRoomId(chatRoom.getId())
			.receiverId(chatRoom.getBuyer().getMemberId())
			.receiverNickname(chatRoom.getBuyer().getNickname())
			.receiverProfileImageUrl(chatRoom.getBuyer().getProfileImageUrl())
			.unreadCount(preview.getUnreadCount())
			.isDisable(!chatRoom.isBuyerJoined());

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
			.unreadCount(preview.getUnreadCount())
			.isDisable(!chatRoom.isSellerJoined());

		if (preview.getLatestMessage() != null) {
			builder = builder.latestMessageTime(preview.getLatestMessageTime())
				.latestMessage(preview.getLatestMessage());
		}
		return builder.build();
	}

}
