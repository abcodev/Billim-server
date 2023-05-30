package com.web.billim.common.validation;

import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckIdValidator extends AbstractValidator<MemberSignupRequest> {

    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(MemberSignupRequest dto, Errors errors) {
        if(memberRepository.existsByEmail(dto.toEntity().getEmail())){
            errors.rejectValue("email","이메일 중복 오류","이미 사용중인 이메일 입니다");
        }
    }
}
