package com.web.billim.chat.service;

import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.domain.service.ChatMessageDomainService;
import com.web.billim.chat.dto.request.SendTextMessageRequest;
import com.web.billim.chat.dto.response.ChatMessagePreview;
import com.web.billim.chat.dto.response.ChatMessageResponse;
import com.web.billim.chat.dto.response.ChatRoomAndPreviewResponse;
import com.web.billim.chat.dto.response.ChatRoomResponse;
import com.web.billim.chat.repository.ChatRoomRepository;
import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.product.domain.Product;
import com.web.billim.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatMessageService chatMessageService;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageDomainService chatMessageDomainService;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	@Transactional
	public ChatRoomResponse joinChatRoom(long memberId, long productId) {
		ChatRoom chatRoom = chatRoomRepository.findByProductIdAndBuyerId(productId, memberId)
			.map(ChatRoom::reJoin)
			.orElseGet(() -> {
				Member member = memberRepository.findById(memberId).orElseThrow();
				Product product = productRepository.findById(productId).orElseThrow();
				ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.of(member, product));
				chatMessageService.sendSystem(SendTextMessageRequest.ofJoinMessage(savedChatRoom, member));
				return savedChatRoom;
			});
		return ChatRoomResponse.from(chatRoom);
	}

	// 이건 서비스에서 처리하기에는 약간.. 너무 구체적이고 복잡한거같다.
	// 뭔가 이 일을 전문적으로 잘 처리해줄 수 있는 객체가 있으면 좋을거같은데..
	// 나는 그냥 걔한테 시키고 결과만 받는 입장이 되면 좋겠다..!
	@Transactional
	public List<ChatMessageResponse> retrieveAllChatMessage(long readMemberId, long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		chatMessageDomainService.readAll(readMemberId, chatRoom);
		return chatMessageDomainService.findAll(chatRoom).stream()
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
	public List<ChatRoomAndPreviewResponse> retrieveAllJoined(long memberId) {
		Stream<ChatRoomAndPreviewResponse> buyChatRoomStream = chatRoomRepository.findAllJoinedByBuyerId(memberId).stream()
			.map(chatRoom -> {
				ChatMessagePreview preview = chatMessageService.retrieveChatMessagePreview(chatRoom);
				return ChatRoomAndPreviewResponse.forBuyer(chatRoom, preview);
			});
		Stream<ChatRoomAndPreviewResponse> sellChatRoomStream = chatRoomRepository.findAllJoinedBySellerId(memberId).stream()
			.map(chatRoom -> {
				ChatMessagePreview preview = chatMessageService.retrieveChatMessagePreview(chatRoom);
				return ChatRoomAndPreviewResponse.forSeller(chatRoom, preview);
			});

		return Stream.concat(buyChatRoomStream, sellChatRoomStream)
			.sorted((x, y) -> (int) (x.getLatestMessageTime().toEpochSecond(ZoneOffset.UTC) - y.getLatestMessageTime().toEpochSecond(ZoneOffset.UTC)))
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
