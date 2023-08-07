package com.web.billim.chat.service;

import java.util.Optional;

import com.web.billim.infra.ImageUploadService;
import org.springframework.stereotype.Service;

import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.dto.response.ChatMessagePreview;
import com.web.billim.chat.dto.response.ChatMessageResponse;
import com.web.billim.chat.dto.request.SendImageMessageRequest;
import com.web.billim.chat.dto.request.SendTextMessageRequest;
import com.web.billim.chat.repository.ChatMessageRepository;
import com.web.billim.chat.repository.ChatRoomRepository;
import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberRepository memberRepository;
	private final ImageUploadService imageUploadService;

	public ChatMessageResponse sendText(SendTextMessageRequest req) {
		ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

		Member sender = memberRepository.findById(req.getSenderId()).orElseThrow();
		ChatMessage message = ChatMessage.ofText(sender, chatRoom, req.getMessage());
		ChatMessage saved = chatMessageRepository.save(message);
		return ChatMessageResponse.from(saved);
	}

	public ChatMessageResponse sendSystem(SendTextMessageRequest req) {
		ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

		ChatMessage message = ChatMessage.ofSystem(chatRoom, req.getMessage());
		ChatMessage saved = chatMessageRepository.save(message);
		return ChatMessageResponse.from(saved);
	}

	public ChatMessageResponse sendImage(SendImageMessageRequest req) {
		Member sender = memberRepository.findById(req.getSenderId()).orElseThrow();
		ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

		String imageUrl = imageUploadService.upload(req.getEncodedImage(), "chat_" + req.getChatRoomId());
		ChatMessage message = ChatMessage.ofImage(sender, chatRoom, imageUrl);
		ChatMessage saved = chatMessageRepository.save(message);
		return ChatMessageResponse.from(saved);
	}

	public ChatMessagePreview retrieveChatMessagePreview(ChatRoom chatRoom) {
		return chatMessageRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom)
			.map(latestMessage -> {
				int unreadCount = chatMessageRepository.calculateUnreadCount(chatRoom, chatRoom.getProduct().getMember());
				return ChatMessagePreview.of(latestMessage, unreadCount);
			}).orElse(ChatMessagePreview.empty());
	}

}
