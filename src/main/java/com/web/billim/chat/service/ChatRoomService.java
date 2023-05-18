package com.web.billim.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.dto.ChatMessageResponse;
import com.web.billim.chat.dto.ChatRoomResponse;
import com.web.billim.chat.dto.SendMessageRequest;
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

	public void send(SendMessageRequest req) {
		Member sender = memberRepository.findById(req.getSenderId()).orElseThrow();
		ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

		if ("IMAGE".equals(req.getType())) {
			String imageUrl = imageUploadService.upload(req.getEncodedBase64Image(), "chat_" + req.getChatRoomId());
			ChatMessage message = ChatMessage.ofImage(sender, chatRoom, imageUrl);
			chatMessageRepository.save(message);
		} else if ("TEXT".equals(req.getType())) {
			ChatMessage message = ChatMessage.ofText(sender, chatRoom, req.getMessage());
			chatMessageRepository.save(message);
		}
	}

}
