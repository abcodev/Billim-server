package com.web.billim.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendImageMessageRequest {

	private long chatRoomId;
	private long senderId;
	private String encodedImage;

}
