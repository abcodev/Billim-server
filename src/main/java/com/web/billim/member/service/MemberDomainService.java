package com.web.billim.member.service;

import com.web.billim.chat.service.ChatRoomService;
import com.web.billim.common.infra.ImageUploadService;
import com.web.billim.coupon.repository.CouponRepository;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.email.service.EmailSendService;
import com.web.billim.exception.NotFoundException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.point.service.PointService;
import com.web.billim.product.repository.ProductRepository;
import com.web.billim.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberDomainService {

    private final MemberRepository memberRepository;
    private final CouponService couponService;
    private final PointService pointService;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ChatRoomService chatRoomService;

    public Member retrieve(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Member unregister(long memberId) {
        Member member = this.retrieve(memberId);
        productRepository.findAllByMember_memberId(memberId)
                .forEach(product -> productService.delete(memberId, product.getProductId()));

        memberRepository.save(member.unregister());

        pointService.deleteByUnregister(memberId);
        couponService.deleteByUnregister(memberId);

        chatRoomService.retrieveAllJoined(memberId)
                .forEach(room -> chatRoomService.exit(memberId, room.getChatRoomId()));
        return member;
    }
}
