package com.web.billim.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.dto.ChatMessageResponse;
import com.web.billim.chat.dto.ChatRoomAndPreviewResponse;
import com.web.billim.chat.dto.ChatRoomResponse;
import com.web.billim.chat.dto.SendImageMessageRequest;
import com.web.billim.chat.dto.SendTextMessageRequest;
import com.web.billim.chat.repository.ChatMessageRepository;
import com.web.billim.chat.repository.ChatRoomRepository;
import com.web.billim.infra.ImageUploadService;
import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.product.domain.Product;
import com.web.billim.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final ImageUploadService imageUploadService;

	public ChatRoomResponse generateIfAbsent(long memberId, long productId) {
		ChatRoom chatRoom = chatRoomRepository.findByProductIdAndMemberId(productId, memberId)
			.orElseGet(() -> {
				Member member = memberRepository.findById(memberId).orElseThrow();
				Product product = productRepository.findById(productId).orElseThrow();
				return chatRoomRepository.save(ChatRoom.of(member, product));
			});
		return ChatRoomResponse.from(chatRoom);
	}

	public List<ChatMessageResponse> retrieveAllChatMessage(long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		return chatMessageRepository.findAllByChatRoom(chatRoom).stream()
			.map(ChatMessageResponse::from)
			.collect(Collectors.toList());
	}

	public ChatMessageResponse sendText(SendTextMessageRequest req) {
		Member sender = memberRepository.findById(req.getSenderId()).orElseThrow();
		ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

		ChatMessage message = ChatMessage.ofText(sender, chatRoom, req.getMessage());
		ChatMessage saved = chatMessageRepository.save(message);
		return ChatMessageResponse.from(saved);
	}

	public ChatMessageResponse sendImage(SendImageMessageRequest req) {
		Member sender = memberRepository.findById(req.getSenderId()).orElseThrow();
		ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

		// TODO : FE 에서 Upload 해서 URL 만 넘겨주는 상황이라면 빠지게 될 코드
		// 	      서버에서 업로드 해야한다면 추가되어야 할 코드
		// String imageUrl = imageUploadService.upload(req.getImageUrl(), "chat_" + req.getChatRoomId());
		ChatMessage message = ChatMessage.ofImage(sender, chatRoom, req.getImageUrl());
		ChatMessage saved = chatMessageRepository.save(message);
		return ChatMessageResponse.from(saved);
	}

	@Transactional(readOnly = true)
	public List<ChatRoomAndPreviewResponse> retrieveAllByProductId(long productId) {
		return chatRoomRepository.findByProductId(productId).stream()
			.map(chatRoom -> {
				ChatMessage latestMessage = chatMessageRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom);
				int unreadCount = chatMessageRepository.calculateUnreadCount(chatRoom, chatRoom.getProduct().getMember());
				return ChatRoomAndPreviewResponse.of(chatRoom, latestMessage, unreadCount);
			})
			.collect(Collectors.toList());
	}

	public List<ChatRoomAndPreviewResponse> retrieveAllJoined(long memberId) {
		return List.of();
	}

	// 1. 네트워크 굳이 한번 더 타? FE -> AWS, FE -> BE -> AWS?
	// 2. BE 코드 테스트하기 너무 어려워짐

}
