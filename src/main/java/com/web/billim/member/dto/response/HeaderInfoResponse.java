package com.web.billim.member.dto.response;

import com.web.billim.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HeaderInfoResponse {
    private String profileImageUrl;

    public static HeaderInfoResponse of(Member member) {
        return HeaderInfoResponse.builder()
                .profileImageUrl(member.getProfileImageUrl())
                .build();

    }

}
