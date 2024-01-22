package com.web.billim.member.service;

import com.web.billim.chat.service.ChatRoomService;
import com.web.billim.client.kakao.KakaoOAuthClient;
import com.web.billim.common.infra.ImageUploadService;
import com.web.billim.coupon.repository.CouponRepository;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.email.service.EmailSendService;
import com.web.billim.exception.BadRequestException;
import com.web.billim.exception.NotFoundException;
import com.web.billim.exception.UnAuthorizedException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.command.UpdatePasswordCommand;
import com.web.billim.member.dto.request.FindPasswordRequest;
import com.web.billim.member.dto.request.MemberInfoUpdateRequest;
import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.dto.response.HeaderInfoResponse;
import com.web.billim.member.dto.response.MemberInfoResponse;
import com.web.billim.member.dto.response.MyPageInfoResponse;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.member.type.MemberType;
import com.web.billim.oauth.domain.SocialMember;
import com.web.billim.oauth.dto.OAuthLogin;
import com.web.billim.oauth.repository.OAuthRepository;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.service.PointService;
import com.web.billim.product.repository.ProductRepository;
import com.web.billim.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final CouponService couponService;
    private final PointService pointService;

    private final MemberDomainService memberDomainService;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OAuthRepository oAuthRepository;

    private final KakaoOAuthClient kakaoOAuthClient;

    private final ImageUploadService imageUploadService;
    private final EmailSendService emailSendService;
    private final PasswordEncoder passwordEncoder;


    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ChatRoomService chatRoomService;



    // TODO: validation 정리
    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    // 회원가입
    @Transactional
    public void signUp(MemberSignupRequest memberSignupRequest) {
        memberSignupRequest.PasswordChange(passwordEncoder);
        Member member = memberRepository.save(memberSignupRequest.toEntity());

        // 쿠폰 주기
        couponRepository.findByName("회원가입 쿠폰")
                .map(coupon -> couponService.issueCoupon(member, coupon))
                .orElseThrow();

        // 포인트 주기
        AddPointCommand command = new AddPointCommand(member, 1000, LocalDateTime.now().plusDays(365));
        pointService.addPoint(command);
    }

    // 닉네임 중복 확인
    public boolean checkDuplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    // 헤더 회원 정보
    @Transactional
    public HeaderInfoResponse retrieveHeaderInfo(long memberId) {
        Member member = memberDomainService.retrieve(memberId);
        return HeaderInfoResponse.of(member);
    }

    // 회원정보 수정
    @Transactional
    public void updateInfo(long memberId, MemberInfoUpdateRequest req) {
        Member member = memberDomainService.retrieve(memberId);
        if (!member.getNickname().equals(req.getNickname())) {
            throw new RuntimeException("동일한 닉네임으로 변경할 수 없습니다.");
        }
        if (memberRepository.existsByNickname(req.getNickname())) {
            throw new RuntimeException("중복된 닉네임 입니다.");
        }
        String imageUrl = Optional.ofNullable(req.getNewProfileImage()).map(image -> {
            if (!member.isDefaultImage()) {
                imageUploadService.delete(member.getProfileImageUrl());
            }
            return imageUploadService.upload(req.getNewProfileImage(), "profile");
        }).orElse(null);
        member.updateInfo(imageUrl, req.getNickname(), req.getAddress());
        memberRepository.save(member);
    }

    // 비밀번호 찾기 (임시비밀번호 전송)
    @Transactional
    public void findPassword(FindPasswordRequest req) {
        Member member = memberRepository.findByEmailAndName(req.getEmail(), req.getName())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getMemberType().equals(MemberType.KAKAO)) {
            throw new BadRequestException(ErrorCode.INVALID_MEMBER);
        }

        String tempPassword = emailSendService.sendTempPassword(req);
        String encodedPassword = passwordEncoder.encode(tempPassword);
        member.changePassword(encodedPassword);
        // Dirty Checking
//		memberRepository.save(member);
    }

    // 비밀번호 확인
    public void checkPassword(long memberId, String password) {
        Member member = memberDomainService.retrieve(memberId);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.INVALID_PASSWORD);
        }
    }

    // 비밀번호 재설정
    @Transactional
    public void updatePassword(UpdatePasswordCommand command) {
        Member member = memberDomainService.retrieve(command.getMemberId());
        if (!passwordEncoder.matches(command.getPassword(), member.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.INVALID_EMAIL_PASSWORD);
        }
        String encodedPassword = passwordEncoder.encode(command.getNewPassword());
        member.changePassword(encodedPassword);
    }


//    public Member unregister(long memberId) {
//        Member member = memberDomainService.retrieve(memberId);
//        productRepository.findAllByMember_memberId(memberId)
//                .forEach(product -> productService.delete(memberId, product.getProductId()));
//
//        memberRepository.save(member.unregister());
//
//        pointService.deleteByUnregister(memberId);
//        couponService.deleteByUnregister(memberId);
//
//        chatRoomService.retrieveAllJoined(memberId)
//                .forEach(room -> chatRoomService.exit(memberId, room.getChatRoomId()));
//        return member;
//    }

    public Member unregister(long memberId) {
        Member member = memberDomainService.retrieve(memberId);
        productRepository.findAllByMember_memberId(memberId)
                .forEach(product -> productService.delete(memberId, product.getProductId()));

        memberRepository.save(member.unregister());

        pointService.deleteByUnregister(memberId);
        couponService.deleteByUnregister(memberId);

        chatRoomService.retrieveAllJoined(memberId)
                .forEach(room -> chatRoomService.exit(memberId, room.getChatRoomId()));
        return member;
    }

    // 회원 탈퇴
    @Transactional
    public void unregister(long memberId, String password) {
        Member member = memberDomainService.retrieve(memberId);
        member.validatePassword(passwordEncoder, password);
        this.unregister(memberId);
    }

    @Transactional
    public void unregisterSocialMember(long memberId) {
        Member member = this.unregister(memberId);
        oAuthRepository.findByMember(member).ifPresent(socialMember -> {
            kakaoOAuthClient.unlink(socialMember.getRefreshToken());
            oAuthRepository.delete(socialMember);
        });
    }

    // 카카오 회원가입
    public Member register(OAuthLogin kakaoLogin) {
        String nickname;
        do {
            nickname = "Billim-" + RandomStringUtils.random(7, true, true);
        }
        while (memberRepository.existsByNickname(nickname));

        Member member = Member.builder()
                .email(kakaoLogin.getEmail())
                .password(" ")
                .name(kakaoLogin.getName())
                .nickname(nickname)
                .grade(MemberGrade.BRONZE)
                .memberType(MemberType.KAKAO)
                .profileImageUrl(kakaoLogin.getImageUrl())
                .address(" ")
                .useYn("Y")
                .build();
        return memberRepository.save(member);
    }

    // 마이페이지 회원 정보
    @Transactional
    public MyPageInfoResponse retrieveMyPageInfo(long memberId) {
        return memberRepository.findById(memberId).map(member -> {
            long availableAmount = pointService.retrieveAvailablePoint(memberId);
            long availableCouponCount = couponService.retrieveAvailableCouponList(memberId).size();
            return MyPageInfoResponse.of(member, availableAmount, availableCouponCount);
        }).orElseThrow();
    }

    // 회원 정보 수정시 기존 정보 조회
    @Transactional
    public MemberInfoResponse retrieveUpdateInfoPage(long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberInfoResponse::from)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 등급 체크
    public void memberGradeCheck() {
        // 회원들의 총 구매 금액을 확인 → 10만원 이상 30만원 미만 회원이라면 등급을 확인해서 bronze 면 실버로
        // 실버라면 기존 유지 ...
        List<Long> memberIdLists = memberRepository.findAllMemberId();

        memberIdLists.forEach(memberId -> {

            Long totalPurchaseAmount = calculateTotalPurchaseAmount(memberId);
            log.debug(memberId + "의 총 구매금액은 " + totalPurchaseAmount);

            MemberGrade memberGrade = calculateNewGrade(totalPurchaseAmount);
            log.debug(memberId + "의 바뀐등급은 " + memberGrade);

            memberRepository.findById(memberId)
                    .filter(member -> !(member.getGrade().equals(memberGrade)))
                    .ifPresent(member -> {
                        member.setGrade(memberGrade);
                        memberRepository.save(member);
                    });
        });
    }

    private long calculateTotalPurchaseAmount(Long memberId) {
        List<ProductOrder> productOrders = orderRepository.findByMember_memberId(memberId);
        return productOrders.stream()
                .mapToLong(ProductOrder::getPrice)
                .sum();
    }

    private MemberGrade calculateNewGrade(long totalPurchaseAmount) {
        if (totalPurchaseAmount <= 100000) {
            return MemberGrade.BRONZE;
        } else if (totalPurchaseAmount <= 300000) {
            return MemberGrade.SILVER;
        } else if (totalPurchaseAmount <= 500000) {
            return MemberGrade.GOLD;
        } else {
            return MemberGrade.DIAMOND;
        }
    }

}

