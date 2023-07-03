package com.web.billim.chat.dto.response;

import com.web.billim.chat.domain.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {

	private long chatRoomId;

	public static ChatRoomResponse from(ChatRoom chatRoom) {
		return new ChatRoomResponse(chatRoom.getId());
	}

}
