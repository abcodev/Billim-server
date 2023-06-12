package com.web.billim.member.service;

import com.web.billim.common.email.dto.EmailAuthRequest;
import com.web.billim.common.email.dto.EmailRequest;
import com.web.billim.common.exception.DuplicateEmailException;
import com.web.billim.common.email.service.EmailService;
import com.web.billim.coupon.repository.CouponRepository;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.infra.ImageUploadService;
import com.web.billim.jwt.JwtTokenRedisService;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final ImageUploadService imageUploadService;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final PointService pointService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenRedisService jwtTokenRedisService;
    private final EmailService emailService;

    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

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

    public void certifyEmail(EmailRequest request) {
        validateDuplicated(request.getEmail());
        String authToken = UUID.randomUUID().toString();
        jwtTokenRedisService.saveEmailToken(request.getEmail(),authToken);
        emailService.sendMail(request.getEmail(),authToken);
    }

    public void validateDuplicated(String email){
        if(memberRepository.existsByEmail(email)){
            throw new DuplicateEmailException();
        }
    }

    public void confirmEmail(EmailAuthRequest emailAuthRequest) {
        if(!jwtTokenRedisService.findByEmail(emailAuthRequest.getEmail())
                .equals(emailAuthRequest.getAuthToken())){
            throw new RuntimeException("인증번호가 일치하지 않습니다.");
        }
    }

    public Member retrieve(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 사용자(" + memberId + ") 를 찾을 수 없습니다."));
    }

    @Transactional
    public void updateProfileImage(long memberId, MultipartFile profileImage) {
        String imageUrl = imageUploadService.upload(profileImage, "profile");
        memberRepository.findById(memberId)
                .ifPresent(member -> {
                    member.updateProfileImage(imageUrl);
                    memberRepository.save(member);
                });
    }

    @Transactional
    public void updateAddress(long memberId, String address) {
        memberRepository.findById(memberId)
                .ifPresent(member -> {
                    member.updateAddress(address);
                    memberRepository.save(member);
                });
    }

    @Transactional
    public void updateNickname(long memberId, String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new RuntimeException("중복된 닉네임 입니다.");
        }
        memberRepository.findById(memberId)
                .ifPresent(member -> {
                    member.updateNickname(nickname);
                    memberRepository.save(member);
                });
    }

    @Transactional
    public boolean checkDuplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

}


// A 라는 사용자가 a, b, c 라는 상품을 올린다.
// B 라는 사용자가 a 도 채팅, b 도 채팅, c 도 채팅을 열었다.
// B 라는 사용자가 a 채팅창에서 차단
// 그러면 b, c 채팅방에선 어떻게될까?
//  -> 구매중인 목록이 있으면 차단 못하게..?