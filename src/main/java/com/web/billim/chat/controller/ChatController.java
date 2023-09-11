package com.web.billim.chat.controller;

import java.util.List;

import com.web.billim.chat.dto.request.ChatReadRequest;
import com.web.billim.chat.dto.response.ChatRoomProductInfo;
import com.web.billim.chat.service.ChatMessageSocketSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.web.billim.chat.dto.response.ChatMessageResponse;
import com.web.billim.chat.dto.response.ChatRoomAndPreviewResponse;
import com.web.billim.chat.dto.response.ChatRoomResponse;
import com.web.billim.chat.dto.request.SendImageMessageRequest;
import com.web.billim.chat.dto.request.SendTextMessageRequest;
import com.web.billim.chat.service.ChatMessageService;
import com.web.billim.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Slf4j
@Tag(name = "채팅", description = "ChatController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatRoomService chatRoomService;
	private final ChatMessageService chatMessageService;
	private final ChatMessageSocketSendService chatMessageSocketSendService;

	@Operation(summary = "구매자가 채팅방 생성", description = "구매자가 처음으로 채팅방을 생성하고, 채팅방이 있으면 그냥 입장한다.")
	@PostMapping("/room/product/{productId}")
	public ResponseEntity<ChatRoomResponse> joinChatRoomForBuyer(@PathVariable long productId, @AuthenticationPrincipal long memberId) {
		return ResponseEntity.ok(chatRoomService.joinChatRoom(memberId, productId));
	}

	@Operation(summary = "판매자가 채팅방 생성", description = "판매자가가 판매목록에서 채팅방 생성한다.")
	@PostMapping("/room/product/{productId}/{buyerId}")
	public ResponseEntity<ChatRoomResponse> joinChatRoomForSeller(@PathVariable long productId, @PathVariable long buyerId) {
		return ResponseEntity.ok(chatRoomService.joinChatRoom(buyerId, productId));
	}

	@Operation(summary = "채팅방 목록 조회", description = "자신의 채팅방 목록 전체 조회한다.")
	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoomAndPreviewResponse>> retrieveAllMyChatRoom(@AuthenticationPrincipal long buyerId) {
		return ResponseEntity.ok(chatRoomService.retrieveAllJoined(buyerId));
	}

	@Operation(summary = "판매자의 productId 에 따른 채팅방 목록 조회", description = "해당 상품에 대한 채팅방 전체 목록을 가져온다.")
	@GetMapping("/api/chat")
	public ResponseEntity<List<ChatRoomAndPreviewResponse>> retrieveAllProductChatRoom(@PathVariable long productId) {
		return ResponseEntity.ok(chatRoomService.retrieveAllByProductId(productId));
	}

	// TODO : 나간 후 다시 재입장했을 때 나가기 전 메시지 가리기
	@Operation(summary = "채팅방 들어갔을 때 채팅 내용 조회", description = "채팅방 들어갔을 때 전체 채팅 목록을 불러온다.")
	@GetMapping("/messages/{chatRoomId}")
	public ResponseEntity<List<ChatMessageResponse>> retrieveAllChatMessage(
			@AuthenticationPrincipal long memberId,
			@PathVariable long chatRoomId
	) {
		return ResponseEntity.ok(chatRoomService.retrieveAllChatMessage(memberId, chatRoomId));
	}

	@Operation(summary = "채팅방 해당 상품 정보", description = "해당 채팅방에 해당하는 상품 정보를 조회한다.")
	@GetMapping("/product-info/{chatRoomId}")
	public ResponseEntity<ChatRoomProductInfo> retrieveProductInfo(@PathVariable long chatRoomId) {
		return ResponseEntity.ok(chatRoomService.getChatRoomProductInfo(chatRoomId));
	}

	@Operation(summary = "채팅방 나가기", description = "채팅방을 나가면 상대방에게 채팅방 나갔다는 시스템 메세지가 전송된다.")
	@DeleteMapping("/room/{chatRoomId}")
	public void exitChatRoom(@AuthenticationPrincipal long memberId, @PathVariable long chatRoomId) {
		chatRoomService.exit(memberId, chatRoomId);
	}

	@Operation(summary = "채팅 text 전송", description = "텍스트 형식의 채팅을 보낸다.")
	@MessageMapping("/send/text")
	public void sendMessage(SendTextMessageRequest req) {
		System.out.println("req = " + req);
		ChatMessageResponse message = chatMessageService.sendText(req);
		chatMessageSocketSendService.sendMessage(req.getChatRoomId(), message);
	}

	@Operation(summary = "채팅 이미지 전송", description = "이미지 형식의 채팅을 보낸다.")
	@MessageMapping("/send/image")
	public void sendMessage(SendImageMessageRequest req) {
		log.info("인코딩 : " + req);
		ChatMessageResponse message = chatMessageService.sendImage(req);
		log.info("메세지 : " + message);
		chatMessageSocketSendService.sendMessage(req.getChatRoomId(), message);
	}

	// 소켓으로 신규 메시지(isNewMessage = true)가 오면 /message/read 를 호출
	// 내 메시지가 아닐때만 호출해야하는데, 이 분기문이 FE 에서 빠진 것 같다.
	//   => FE 에서 처리하거나
	
	// - 구조를 아예 바꾼다. 어떻게 바꿀지는 모르겠다?

	//  1. 목록만 보고있어도 웹소켓은 구독한 상태기 때문에 상대방 메시지가오면 /message/read API 를 호출해버린다.
			// - FE 에서 현재 들어가있는 채팅방이 뭔지를 따로 가지고 있는게 어떨지..?
	//  2. 내가 보낸 메시지도 웹소켓으로 다시 오니까, 내 메시지를 읽어버리는 상황이 생길수도 있다.
	@Operation(summary = "채팅 읽음 여부", description = "true: 새로운 메세지, false: 기존 메세지, true 일 경우에만 메세지를 새로 보여줍니다.")
	@PostMapping("/message/read")
	public void readMessage(@AuthenticationPrincipal long memberId, @RequestBody ChatReadRequest req) {
		ChatMessageResponse message = chatMessageService.read(memberId, req.getMessageId());
		chatMessageSocketSendService.sendMessage(req.getChatRoomId(), message);
	}

//	@Operation(summary = "채팅 읽음 여부", description = "true: 새로운 메세지, false: 기존 메세지, true 일 경우에만 메세지를 새로 보여줍니다.")
//	@PostMapping("/message/read")
//	public void readMessage(@RequestBody ChatReadRequest req) {
//		ChatMessageResponse message = chatMessageService.read(req.getMessageId());
//		chatMessageSocketSendService.sendMessage(req.getChatRoomId(), message);
//	}


}
