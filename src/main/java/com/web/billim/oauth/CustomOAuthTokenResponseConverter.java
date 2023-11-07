package com.web.billim.oauth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class CustomOAuthTokenResponseConverter implements Converter<Map<String, Object>, OAuth2AccessTokenResponse> {

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, Object> response) {
        String accessToken = (String) response.get(OAuth2ParameterNames.ACCESS_TOKEN);
        long expiresIn = (Integer) response.get(OAuth2ParameterNames.EXPIRES_IN);

        String refreshToken = (String) response.get(OAuth2ParameterNames.REFRESH_TOKEN);
        long refreshTokenExpiresIn = (Integer) response.get("refresh_token_expires_in");

        var additionalParams = new HashMap<String, Object>();
        additionalParams.put(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken);
        additionalParams.put("refresh_token_expires_in", refreshTokenExpiresIn);

        Set<String> scopes = Collections.emptySet();
        if (response.containsKey(OAuth2ParameterNames.SCOPE)) {
            String scope = (String) response.get(OAuth2ParameterNames.SCOPE);
            scopes = Arrays.stream(StringUtils.delimitedListToStringArray(scope, " "))
                    .collect(Collectors.toSet());
        }

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(expiresIn)
                .scopes(scopes)
                .refreshToken(refreshToken)
                .additionalParameters(Collections.unmodifiableMap(additionalParams))
                .build();
    }
}
