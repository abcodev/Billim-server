package com.web.billim.chat.service;

import com.web.billim.chat.domain.ChatRoom;
import com.web.billim.chat.dto.request.SendTextMessageRequest;
import com.web.billim.chat.dto.response.*;
import com.web.billim.chat.repository.ChatRoomRepository;
import com.web.billim.exception.NotFoundException;
import com.web.billim.exception.handler.ErrorCode;
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
    private final ChatMessageSocketSendService chatMessageSocketSendService;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 채팅방 입장
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

    // 전체 채팅 메세지 조회
    @Transactional
    public List<ChatMessageResponse> retrieveAllChatMessage(long readMemberId, long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        chatMessageDomainService.readAll(readMemberId, chatRoom);
        return chatMessageDomainService.findAll(chatRoom).stream()
                .map(message -> {
                    var resp = ChatMessageResponse.updatedMessage(message);
                    chatMessageSocketSendService.sendMessage(chatRoomId, resp);
                    return resp;
                })
                .collect(Collectors.toList());
    }

    // 채팅방 상품 정보 조회
    @Transactional
    public ChatRoomProductInfo getChatRoomProductInfo(long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .map(ChatRoomProductInfo::from)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomAndPreviewResponse> retrieveAllByProductId(long productId) {
        return chatRoomRepository.findAllByProductId(productId).stream()
                .filter(ChatRoom::isSellerJoined)
                .map(chatRoom -> {
                    ChatMessagePreview preview = chatMessageService.retrieveChatMessagePreview(chatRoom, chatRoom.getProduct().getMember().getMemberId());
                    return ChatRoomAndPreviewResponse.forSeller(chatRoom, preview);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomAndPreviewResponse> retrieveAllJoined(long memberId) {
		// 내가 구매자로 들어가 있는 채팅방 목록 조회
        Stream<ChatRoomAndPreviewResponse> buyChatRoomStream = chatRoomRepository.findAllJoinedByBuyerId(memberId).stream()
                .map(chatRoom -> {
                    ChatMessagePreview preview = chatMessageService.retrieveChatMessagePreview(chatRoom, memberId);
                    return ChatRoomAndPreviewResponse.forBuyer(chatRoom, preview);
                });

		// 내가 판매자로 들어가 있는 채팅방 목록 조회
        Stream<ChatRoomAndPreviewResponse> sellChatRoomStream = chatRoomRepository.findAllJoinedBySellerId(memberId).stream()
                .map(chatRoom -> {
                    ChatMessagePreview preview = chatMessageService.retrieveChatMessagePreview(chatRoom, memberId);
                    return ChatRoomAndPreviewResponse.forSeller(chatRoom, preview);
                });

		// 두 목록을 합쳐서 마지막 메시지 순서대로 내림차순 정렬
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
