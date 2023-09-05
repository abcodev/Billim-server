package com.web.billim.chat.service;

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
import org.springframework.transaction.annotation.Transactional;

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
        return ChatMessageResponse.createNewMessage(saved);
    }

    public ChatMessageResponse sendSystem(SendTextMessageRequest req) {
        ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

        ChatMessage message = ChatMessage.ofSystem(chatRoom, req.getMessage());
        ChatMessage saved = chatMessageRepository.save(message);
        return ChatMessageResponse.createNewMessage(saved);
    }

    // 이미지 s3 서버에서 업로드
    public ChatMessageResponse sendImage(SendImageMessageRequest req) {
        Member sender = memberRepository.findById(req.getSenderId()).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();

//        String imageUrl = imageUploadService.upload(req.getEncodedImage(), "chat/chat_" + req.getChatRoomId());
        ChatMessage message = ChatMessage.ofImage(sender, chatRoom, req.getEncodedImage());
        ChatMessage saved = chatMessageRepository.save(message);
        return ChatMessageResponse.createNewMessage(saved);
    }

    // 이미지 s3 프론트에서 업로드
//    public ChatMessageResponse sendImage(SendImageMessageRequest req) {
//        Member sender = memberRepository.findById(req.getSenderId()).orElseThrow();
//        ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId()).orElseThrow();
//
//        // TODO : FE 에서 Upload 해서 URL 만 넘겨주는 상황이라면 빠지게 될 코드
//        // 	      서버에서 업로드 해야한다면 추가되어야 할 코드
//        // String imageUrl = imageUploadService.upload(req.getImageUrl(), "chat_" + req.getChatRoomId());
//        ChatMessage message = ChatMessage.ofImage(sender, chatRoom, req.getImageUrl());
//        ChatMessage saved = chatMessageRepository.save(message);
//        return ChatMessageResponse.from(saved);
//    }


    public ChatMessagePreview retrieveChatMessagePreview(ChatRoom chatRoom, long readMemberId) {
        return chatMessageRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom)
                .map(latestMessage -> {
                    int unreadCount = chatMessageRepository.calculateUnreadCount(chatRoom, readMemberId);
                    return ChatMessagePreview.of(latestMessage, unreadCount);
                }).orElse(ChatMessagePreview.empty());
    }

    @Transactional
    public ChatMessageResponse read(long messageId) {
        return chatMessageRepository.findById(messageId)
                .map(message -> {
                    message.read();
                    return ChatMessageResponse.updatedMessage(message);
                }).orElseThrow();
    }

}
