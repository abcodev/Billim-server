package com.web.billim.member.service;

import com.web.billim.chat.service.ChatRoomService;
import com.web.billim.exception.BadRequestException;
import com.web.billim.exception.JwtException;
import com.web.billim.email.service.EmailSendService;
import com.web.billim.exception.NotFoundException;
import com.web.billim.exception.UnAuthorizedException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.coupon.repository.CouponRepository;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.common.infra.ImageUploadService;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.request.*;
import com.web.billim.member.dto.command.UpdatePasswordCommand;
import com.web.billim.member.dto.response.HeaderInfoResponse;
import com.web.billim.member.dto.response.MyPageInfoResponse;
import com.web.billim.member.dto.response.MemberInfoResponse;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.member.type.MemberType;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.repository.OrderRepository;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.service.PointService;

import com.web.billim.oauth.dto.OAuthLogin;
import com.web.billim.product.domain.Product;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberDomainService memberDomainService;
    private final ImageUploadService imageUploadService;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final PointService pointService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSendService emailSendService;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ChatRoomService chatRoomService;

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

    public boolean checkDuplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

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

    // 회원정보 수정
    @Transactional
    public void updateInfo(long memberId, MemberInfoUpdateRequest req) {
        memberRepository.findById(memberId).ifPresent(member -> {
            if (!member.getNickname().equals(req.getNickname())
                    && memberRepository.existsByNickname(req.getNickname())) {
                throw new RuntimeException("중복된 닉네임 입니다.");
            }
            String imageUrl = null;
            if (req.getNewProfileImage() != null && !req.getNewProfileImage().isEmpty()) {
                imageUploadService.delete(member.getProfileImageUrl());
                imageUrl = imageUploadService.upload(req.getNewProfileImage(), "profile");
            }

            member.updateInfo(imageUrl, req.getNickname(), req.getAddress());
            memberRepository.save(member);
        });
    }

    // 비밀번호 찾기 (임시비밀번호 전송)
    // TODO: 소셜 회원 임시비밀번호 못받게
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


    // 회원 탈퇴
//    @Transactional
//    public void unregister(long memberId, String password) {
//
//        log.info("memberId : " + memberId );
//
////		Member member = memberRepository.findById(memberId)
////				.orElseThrow();
//        Member member = memberDomainService.retrieve(memberId);
//        log.info("11111111111");
//        if(!passwordEncoder.matches(password, member.getPassword())) {
//            throw new UnAuthorizedException(ErrorCode.INVALID_PASSWORD);
//        }
//
////		this.checkPassword(memberId, password);
//
//        // 판매 상품 상태 변화
//        log.info("==========판매 상품 상태 변화===========");
//        List<Product> productList = productRepository.findAllByMember_memberId(memberId)
//                .stream().map(product -> {
//                    product.setDeleted(true);
//                    return product;
//                }).collect(Collectors.toList());
//        productRepository.saveAll(productList);
//
//        // 회원 상태 변화
//        log.info("==========회원 상태 변화===========");
//        member.setUseYn("N");
//
//        log.info("==========적립금 쿠폰 삭제===========");
//        pointService.deleteByUnregister(memberId);
//        couponService.deleteByUnregister(memberId);
//
//    }

    // 회원 탈퇴
    @Transactional
    public void unregister(long memberId, String password) {
        log.info("memberId : " + memberId);
        Member member = memberDomainService.retrieve(memberId);
        member.validatePassword(passwordEncoder, password);

        log.info("==========판매 상품 상태 변화===========");
        // FIXME : deleteAll(memberId)
        productRepository.findAllByMember_memberId(memberId)
                .forEach(product -> productService.delete(memberId, product.getProductId()));

        log.info("==========회원 상태 변화===========");
        memberRepository.save(member.unregister());

        log.info("==========적립금 쿠폰 삭제===========");
        pointService.deleteByUnregister(memberId);
        couponService.deleteByUnregister(memberId);

        chatRoomService.retrieveAllJoined(memberId)
                .forEach(room -> chatRoomService.exit(memberId, room.getChatRoomId()));

        /**
         *  [ Layered Architecture ]
         *  3계층 : Controller - Service - Repository
         *  4계층 : Controller - Application Service - Domain(Entity, Domain Service) - Repository
         *          (Presentation - Service - Domain - Infra)
         *
         *  이 MemberService 는 Application Service Layer 입니다.
         *  Application Service(Facade Service) 를 개발할 때 신경써야 할 것.
         *   1. 비즈니스의 요구사항을 잘 드러내야한다.
         *   2. 요구사항 - UseCase
         *   3. 위에서 아래로 설명하듯이 읽히는 적당히 추상화된 내용이 들어가도록.
         *
         *    - 앞으로 나아가면서 설명하듯이 읽혀야 한다.
         *    - 적당히 추상화가 되어야 한다.
         *    - 복잡한 코드는 더 잘하는 객체를 찾아서 시켜야한다.
         *      그 대상이 다른 Application Service 가 될수도 있고, Domain Entity 가 될수도 있고,
         *      Domain Service 가 될 수도 있다.
         */
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
                .profileImageUrl(kakaoLogin.getImageUrl())
                .address(" ")
                .build();
        return memberRepository.save(member);
    }


    @Transactional
    public HeaderInfoResponse retrieveHeaderInfo(long memberId) {
        Member member = memberDomainService.retrieve(memberId);
        return HeaderInfoResponse.of(member);
    }

//	public Boolean existByEmail(String email) {
//		return memberRepository.existsByEmail(email);
//	}


    // 등급 체크
    public void memberGradeCheck() {
        // 회원들의 총 구매 금액을 확인 → 10만원 이상 30만원 미만 회원이라면 등급을 확인해서 bronze 면 실버로
        // 실버라면 기존 유지 ...
        List<Long> memberIdLists = memberRepository.findAllMemberId();

        memberIdLists.forEach(memberId -> {

            Long totalPurchaseAmount = calculateTotalPurchaseAmount(memberId);
            log.info(memberId + "의 총 구매금액은 " + totalPurchaseAmount);

            MemberGrade memberGrade = calculateNewGrade(totalPurchaseAmount);
            log.info(memberId + "의 바뀐등급은 " + memberGrade);

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

