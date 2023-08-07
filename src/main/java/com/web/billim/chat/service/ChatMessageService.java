package com.web.billim.chat.service;

import java.util.Optional;

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

		// TODO : FE 에서 Upload 해서 URL 만 넘겨주는 상황이라면 빠지게 될 코드
		// 	      서버에서 업로드 해야한다면 추가되어야 할 코드
		// String imageUrl = imageUploadService.upload(req.getImageUrl(), "chat_" + req.getChatRoomId());
		ChatMessage message = ChatMessage.ofImage(sender, chatRoom, req.getImageUrl());
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
