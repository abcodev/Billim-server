package com.web.billim.chat.dto.request;

import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendTextMessageRequest {

	private long chatRoomId;
	private long senderId;
	private String message;

	public static SendTextMessageRequest ofExitMessage(ChatRoom chatRoom, Member exitMember) {
		return new SendTextMessageRequest(
			chatRoom.getId(), 0, String.format("%s 님이 나갔습니다.", exitMember.getName())
		);
	}

	public static SendTextMessageRequest ofJoinMessage(ChatRoom savedChatRoom, Member member) {
		return new SendTextMessageRequest(
			savedChatRoom.getId(), 0, String.format("%s 님이 채팅방을 생성했습니다.", member.getName())
		);
	}

}
