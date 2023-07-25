package com.web.billim.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReIssueTokenResponse {
    private long memberId;
    private String accessToken;
    private String newRefreshToken;

}
