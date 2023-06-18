package com.web.billim.chat.dto;

import java.util.Optional;

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

	// public static SendTextMessageRequest ofJoinMessage(ChatRoom chatRoom, Member joinMember) {
	//
	// }

	// 1. IMAGE 를 FE 에서 S3 에 업로드하고, 그 결과 URL 을 서버로 전송
	// 2. IMAGE 를 Base64 인코딩해서 문자열로 만들어서 서버로 전송

}
