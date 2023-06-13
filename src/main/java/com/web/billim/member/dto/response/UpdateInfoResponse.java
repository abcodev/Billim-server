package com.web.billim.member.dto.response;

import com.web.billim.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateInfoResponse {
    private long memberId;
    private String email;
    private String nickname;
    private String address;
    private String profileImageUrl;

    public static UpdateInfoResponse from(Member member) {
        return UpdateInfoResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
