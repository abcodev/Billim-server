package com.web.billim.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.dto.response.ChatMessagePreview;
import com.web.billim.chat.dto.response.ChatMessageResponse;
import com.web.billim.chat.dto.response.ChatRoomAndPreviewResponse;
import com.web.billim.chat.dto.response.ChatRoomResponse;
import com.web.billim.chat.dto.request.SendTextMessageRequest;
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

	private final ChatMessageService chatMessageService;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final ImageUploadService imageUploadService;

	@Transactional
	public ChatRoomResponse joinChatRoom(long memberId, long productId) {
		ChatRoom chatRoom = chatRoomRepository.findByProductIdAndBuyerId(productId, memberId)
			.map(ChatRoom::reJoin)
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

	@Transactional(readOnly = true)
	public List<ChatRoomAndPreviewResponse> retrieveAllByProductId(long productId) {
		return chatRoomRepository.findAllByProductId(productId).stream()
			.filter(ChatRoom::isSellerJoined)
			.map(chatRoom -> {
				ChatMessagePreview preview = chatMessageService.retrieveChatMessagePreview(chatRoom);
				return ChatRoomAndPreviewResponse.forSeller(chatRoom, preview);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ChatRoomAndPreviewResponse> retrieveAllJoined(long buyerId) {
		return chatRoomRepository.findAllJoinedByBuyerId(buyerId).stream()
			.map(chatRoom -> {
				ChatMessagePreview preview = chatMessageService.retrieveChatMessagePreview(chatRoom);
				return ChatRoomAndPreviewResponse.forBuyer(chatRoom, preview);
			})
			.collect(Collectors.toList());
	}

	@Transactional
	public void exit(long memberId, long chatRoomId) {
		chatRoomRepository.findById(chatRoomId).ifPresent(chatRoom -> {
			Member exitMember = chatRoom.exit(memberId);  // Dirty Checking
			if (chatRoom.checkEmpty()) {
				chatRoomRepository.delete(chatRoom);
			} else {
				chatMessageService.sendSystem(SendTextMessageRequest.ofExitMessage(chatRoom, exitMember));
			}
		});
	}

}
