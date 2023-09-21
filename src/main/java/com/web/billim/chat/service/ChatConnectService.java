package com.web.billim.chat.service;

import com.web.billim.chat.repository.ChatRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatConnectService {

    private final ChatRedisRepository chatRedisRepository;


}
