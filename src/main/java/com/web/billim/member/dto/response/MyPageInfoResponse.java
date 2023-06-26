package com.web.billim.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.billim.member.domain.Member;
import com.web.billim.member.type.MemberGrade;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@ApiModel(value = "마이페이지 회원 정보")
@Setter
@Getter
@Builder
public class MyPageInfoResponse {

    private long memberId;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    private String profileImageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSS")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "사용가능한 적립금 총 금액")
    private long availableAmount;

    private long availableCouponCount;

    private long availableReview;

    public static MyPageInfoResponse of(Member member, long availableAmount, long availableCouponCount) {
        return MyPageInfoResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .grade(member.getGrade())
                .profileImageUrl(member.getProfileImageUrl())
                .createAt(member.getCreatedAt())
                .availableAmount(availableAmount)
                .availableCouponCount(availableCouponCount)
                .build();
    }

}
