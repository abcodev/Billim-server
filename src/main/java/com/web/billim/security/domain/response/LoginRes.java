package com.web.billim.security.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {
    private String accessToken;
    private String refreshToken;
}
