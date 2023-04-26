package com.web.billim.common.validation;

import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckNickNameValidator extends AbstractValidator<MemberSignupRequest>{

    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(MemberSignupRequest dto, Errors errors) {
        if(memberRepository.existsByNickname(dto.toEntity().getNickname())){
            errors.rejectValue("nickname","닉네임 중복 오류","이미 사용중인 닉네임 입니다.");
        }

    }
}
