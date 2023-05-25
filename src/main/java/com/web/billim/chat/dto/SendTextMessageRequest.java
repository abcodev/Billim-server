package com.web.billim.chat.dto;

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

	// 1. IMAGE 를 FE 에서 S3 에 업로드하고, 그 결과 URL 을 서버로 전송
	// 2. IMAGE 를 Base64 인코딩해서 문자열로 만들어서 서버로 전송

}
