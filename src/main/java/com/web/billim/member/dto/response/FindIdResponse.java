package com.web.billim.member.dto.response;

import com.web.billim.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindIdResponse {

    private String email;

    public static FindIdResponse from(Member member) {
        return new FindIdResponse(member.getEmail());
    }
}
