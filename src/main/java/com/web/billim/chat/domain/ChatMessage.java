package com.web.billim.chat.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.web.billim.chat.type.ChatMessageType;
import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "chat_message")
@Builder
@Getter
public class ChatMessage extends JpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_message_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id")
	private ChatRoom chatRoom;

	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "member_id")
	private Member sender; // null

	@Enumerated(EnumType.STRING)
	@Column(name = "message_type")
	private ChatMessageType type;

	private String message;

	@Column(name = "read_yn")
	private boolean read;

	public static ChatMessage ofImage(Member sender, ChatRoom chatRoom, String imageUrl) {
		return ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.type(ChatMessageType.IMAGE)
			.message(imageUrl)
			.read(false)
			.build();
	}

	public static ChatMessage ofText(Member sender, ChatRoom chatRoom, String message) {
		return ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.type(ChatMessageType.TEXT)
			.message(message)
			.read(false)
			.build();
	}

	public static ChatMessage ofSystem(ChatRoom chatRoom, String message) {
		return ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(null)
			.type(ChatMessageType.SYSTEM)
			.message(message)
			.read(true)
			.build();
	}
}
