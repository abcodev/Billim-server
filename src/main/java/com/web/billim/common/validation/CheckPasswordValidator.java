package com.web.billim.common.validation;

import com.web.billim.member.dto.request.MemberSignupRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CheckPasswordValidator extends AbstractValidator<MemberSignupRequest> {
    @Override
    protected void doValidate(MemberSignupRequest dto, Errors errors) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            errors.rejectValue("confirmPassword","비밀번호 일치 오류","비밀번호가 일치하지 않습니다.");
        }
    }
}
