package com.web.billim.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.billim.chat.domain.ChatMessage;
import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.member.domain.Member;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

	List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);

	Optional<ChatMessage> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

	@Query("SELECT count(m) FROM ChatMessage m WHERE m.chatRoom = :chatRoom AND m.read = false AND m.sender is null OR m.sender <> :member")
	int calculateUnreadCount(@Param("chatRoom") ChatRoom chatRoom, @Param("member") Member member);

	@Query("SELECT m FROM ChatMessage m WHERE m.chatRoom = :chatRoom AND m.sender.memberId <> :memberId")
	List<ChatMessage> findAllByOtherMember(@Param("chatRoom") ChatRoom chatRoom, @Param("memberId") long memberId);
}
