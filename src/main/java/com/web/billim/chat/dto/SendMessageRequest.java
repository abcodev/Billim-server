package com.web.billim.chat.dto;

import com.web.billim.chat.domain.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

	private long chatRoomId;
	private long senderId;

	private String type;
	private String message;
	private String encodedBase64Image;

	// 1. IMAGE 를 FE 에서 S3 에 업로드하고, 그 결과 URL 을 서버로 전송
	// 2. IMAGE 를 Base64 인코딩해서 문자열로 만들어서 서버로 전송

}
