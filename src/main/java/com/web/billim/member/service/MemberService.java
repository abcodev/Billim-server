package com.web.billim.member.service;

import com.web.billim.common.dto.EmailAuthRequest;
import com.web.billim.common.dto.EmailRequest;
import com.web.billim.common.handler.TokenExpiredException;
import com.web.billim.common.service.EmailService;
import com.web.billim.coupon.repository.CouponRepository;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.jwt.JwtTokenRedisService;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.request.FindIdRequest;
import com.web.billim.member.dto.request.MemberSignupRequest;
//import com.web.billim.member.dto.response.FindIdResponse;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {

//    private final CouponRepository couponRepository;
//    private final CouponService couponService;
//    private final PointService pointService;
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

    public void signUp(MemberSignupRequest memberSignupRequest) {
        memberSignupRequest.PasswordChange(passwordEncoder);
        memberRepository.save(memberSignupRequest.toEntity());
//        // 쿠폰 주기
//        couponRepository.findByName("회원가입 쿠폰")
//                .map(coupon -> couponService.issueCoupon(member, coupon))
//                .orElseThrow();
//
//        // 포인트 주기
//        AddPointCommand command = new AddPointCommand(member, 1000, LocalDateTime.now().plusDays(365));
//        pointService.addPoint(command);
    }

//    public FindIdResponse findId(FindIdRequest findIdRequest) {
//        return memberRepository.findByNameAndEmail(findIdRequest.getName(), findIdRequest.getEmail())
//                .map(FindIdResponse::from)
//                .orElse(new FindIdResponse());
//    }

    public Member retrieve(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 사용자(" + memberId + ") 를 찾을 수 없습니다."));
    }

    public void certifyEmail(EmailRequest request) {
        validateDuplicated(request.getEmail());
        String authToken = UUID.randomUUID().toString();
        jwtTokenRedisService.saveEmailToken(request.getEmail(),authToken);
        emailService.sendMail(request.getEmail(),authToken);
    }

    public void validateDuplicated(String email){
        if(memberRepository.existsByEmail(email)){
           throw new RuntimeException("이미 사용중인 이메일입니다.");
        }
    }

    public void confirmEmail(EmailAuthRequest emailAuthRequest) {
        if(!jwtTokenRedisService.findByEmail(emailAuthRequest.getEmail())
                .equals(emailAuthRequest.getAuthToken())){
            throw new RuntimeException("인증번호가 일치하지 않습니다.");

        }
    }
}
