package com.web.billim.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

	public static final String STOMP_ENDPOINT = "/stomp/chat";
	public static final String MESSAGE_BROKER_SUBSCRIBE_PREFIX = "/subscribe";
	public static final String MESSAGE_BROKER_PUBLISH_PREFIX = "/publish";

	// STOMP Endpoint 에 대한 설정 추가
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(STOMP_ENDPOINT)
			.setAllowedOriginPatterns("*")
			.withSockJS(); // SockJS (FE)
	}

	// MessageBroker 에 대한 설정 추가
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker(MESSAGE_BROKER_SUBSCRIBE_PREFIX);
		registry.setApplicationDestinationPrefixes(MESSAGE_BROKER_PUBLISH_PREFIX);
		// FE 에서 특정 Queue 를 구독하고 싶음
		// Queue 는 여러개 들어갈 수 있고, 각각의 큐 이름이 있다면
		// A, B, C, D
		// /subscribe/A
		// /subscribe/B
		// /subscribe/C
		// /subscribe/D

		// /publish/Aa

		/**
		 *  [ 사용자 A ]
		 *   ws://localhost:8080/stomp/chat
		 *    - subscribe URL : /subscribe/chat/{chatRoomId}
		 *
		 *  [ 사용자 B ]
		 *   ws://localhost:8080/stomp/chat
		 *    - send : {"chatRoomId": "{chatRoomId}", "senderId": 1, "message": "hello"}
		 */
	}

}
