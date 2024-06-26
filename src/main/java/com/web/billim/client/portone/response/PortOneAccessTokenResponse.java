package com.web.billim.client.portone.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortOneAccessTokenResponse {
    private String accessToken;
    private String refreshToken;
}
