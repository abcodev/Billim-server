package com.web.billim.member.dto.request;

import com.web.billim.member.domain.Member;
import com.web.billim.member.type.MemberGrade;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
public class MemberSignupRequest {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    private String password2;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "한글로 입력해 주세요.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해아 합니다.")
    private String email;


    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .address(address)
                .email(email)
                .grade(MemberGrade.BRONZE)
                .build();
    }

}
