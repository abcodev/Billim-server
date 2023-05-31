package com.web.billim.jwt;

import com.web.billim.jwt.dto.ReIssueTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtils jwtUtils;
    public void reIssueToken(ReIssueTokenRequest req) {
            Boolean check = jwtUtils.tokenValidation(req.getRefreshToken() );

    }
}
