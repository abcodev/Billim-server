package com.web.billim.chat.controller;

import static com.web.billim.chat.config.ChatConfig.*;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.billim.chat.dto.ChatMessageResponse;
import com.web.billim.chat.dto.ChatRoomAndPreviewResponse;
import com.web.billim.chat.dto.ChatRoomResponse;
import com.web.billim.chat.dto.SendImageMessageRequest;
import com.web.billim.chat.dto.SendTextMessageRequest;
import com.web.billim.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

	// 웹소켓 STOMP 테스트
	// Spring STOMP
	// MessageHandler 를 API Handler 처럼 Controller 에 등록할 수 있음
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatRoomService chatRoomService;

	@ApiOperation(value = "처음 채팅방 생성", notes = "구매자가 처음으로 채팅방을 생성한다.")
	@PostMapping("/room/{productId}")
	public ResponseEntity<ChatRoomResponse> generateChatRoom(@PathVariable long productId, @AuthenticationPrincipal long memberId) {
		return ResponseEntity.ok(chatRoomService.generateIfAbsent(memberId, productId));
	}

	@ApiOperation(value = "판매자의 productId 에 따른 채팅방 목록", notes = "해당 상품에 대한 채팅방 전체 목록을 가져온다.")
	@GetMapping("/rooms/{productId}")
	public ResponseEntity<List<ChatRoomAndPreviewResponse>> retrieveAllProductChatRoom(@PathVariable long productId) {
		return ResponseEntity.ok(chatRoomService.retrieveAllByProductId(productId));
	}

	@ApiOperation(value = "구매자의 채팅방 목록 조회", notes = "자신의 채팅방 목록 전체 조회")
	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoomAndPreviewResponse>> retrieveAllMyChatRoom(@AuthenticationPrincipal long memberId) {
		return ResponseEntity.ok(chatRoomService.retrieveAllJoined(memberId));
	}

	@ApiOperation(value = "채팅방 들어갔을 때 채팅 내용 조회", notes = "채팅방 들어갔을 때 전체 채팅 목록을 불러온다.")
	@GetMapping("/messages/{chatRoomId}")
	public ResponseEntity<List<ChatMessageResponse>> retrieveAllChatMessage(@PathVariable long chatRoomId) {
		return ResponseEntity.ok(chatRoomService.retrieveAllChatMessage(chatRoomId));
	}

	@ApiOperation(value = "채팅 text 전송", notes = "텍스트 형식의 채팅을 보낸다")
	@MessageMapping("/send/text")
	public void sendMessage(SendTextMessageRequest req) {
		ChatMessageResponse message = chatRoomService.sendText(req, false);
		messagingTemplate.convertAndSend(MESSAGE_BROKER_SUBSCRIBE_PREFIX + "/chat/" + req.getChatRoomId(), message);
	}

	@ApiOperation(value = "채팅 이미지 전송", notes = "이미지 형식의 채팅을 보낸다")
	@MessageMapping("/send/image")
	public void sendMessage(SendImageMessageRequest req) {
		ChatMessageResponse message = chatRoomService.sendImage(req);
		messagingTemplate.convertAndSend(MESSAGE_BROKER_SUBSCRIBE_PREFIX + "/chat/" + req.getChatRoomId(), message);
	}

	// 채팅방 나가기
	@ApiOperation(value = "채팅방 나가기", notes = "채팅방을 나가면 상대방에게 채팅방 나갔다는 시스템 메세지가 전송됨")
	@DeleteMapping("/room/{chatRoomId}")
	public void exitChatRoom(@AuthenticationPrincipal long memberId, @PathVariable long chatRoomId) {
		chatRoomService.exit(memberId, chatRoomId);
	}

	// 차단하기

}

/*
	1. FE 에서 S3 저장소에 사진을 업로드하고 URL 만 서버로 주는 것.
	2. ByteCode 로 인코딩해서 전송해주는 방법.
*/
