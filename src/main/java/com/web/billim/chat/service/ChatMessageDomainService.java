package com.web.billim.chat.service;

import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageDomainService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void readAll(long readMemberId, ChatRoom chatRoom) {
        chatMessageRepository.findAllByOtherMember(chatRoom, readMemberId)
                .forEach(ChatMessage::read); // Dirty Checking
    }

    public List<ChatMessage> findAll(ChatRoom chatRoom) {
        return chatMessageRepository.findAllByChatRoom(chatRoom);
    }

}
