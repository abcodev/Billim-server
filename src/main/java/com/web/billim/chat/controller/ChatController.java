package com.web.billim.chat.controller;

import static com.web.billim.chat.config.ChatConfig.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.dto.ChatMessageResponse;
import com.web.billim.chat.dto.ChatRoomResponse;
import com.web.billim.chat.dto.SendMessageRequest;
import com.web.billim.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

	// 웹소켓 STOMP 테스트
	// Spring STOMP
	// MessageHandler 를 API Handler 처럼 Controller 에 등록할 수 있다.
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatRoomService chatRoomService;

	// 처음으로 채팅방 생성하는 API
	@PostMapping("/{productId}")
	public ResponseEntity<ChatRoomResponse> generateChatRoom(@PathVariable long productId, @AuthenticationPrincipal long memberId) {
		return ResponseEntity.ok(chatRoomService.generateIfAbsent(memberId, productId));
	}

	// TODO : 채팅방 목록 조회하는 API (판매자 입장)


	// 채팅방 들어갔을 때 채팅목록 읽어오는 API
	@GetMapping("/{chatRoomId}")
	public ResponseEntity<List<ChatMessageResponse>> retrieveAllChatMessage(@PathVariable long chatRoomId) {
		return ResponseEntity.ok(chatRoomService.retrieveAllChatMessage(chatRoomId));
	}

	// 채팅 보낼때
	@MessageMapping("/send")
	public void sendMessage(SendMessageRequest req) {
		// 데이터베이스에 채팅 데이터를 저장하고..
		chatRoomService.send(req);
		messagingTemplate.convertAndSend(MESSAGE_BROKER_SUBSCRIBE_PREFIX + "/chat/" + req.getChatRoomId(), req.getMessage());
	}

}
