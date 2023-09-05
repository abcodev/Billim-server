package com.web.billim.chat.service;

import com.web.billim.chat.dto.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.web.billim.chat.config.ChatConfig.MESSAGE_BROKER_SUBSCRIBE_PREFIX;

@Service
@RequiredArgsConstructor
public class ChatMessageSocketSendService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(long chatRoomId, ChatMessageResponse message) {
        messagingTemplate.convertAndSend(MESSAGE_BROKER_SUBSCRIBE_PREFIX + "/chat/" + chatRoomId, message);
    }

}
