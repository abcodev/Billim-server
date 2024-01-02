package com.web.billim.client.kakao;

import com.web.billim.client.kakao.dto.KakaoUnlinkResponse;
import com.web.billim.client.kakao.dto.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private final RestTemplate restTemplate;

    private final static String AUTH_BASE_URL = "https://kauth.kakao.com";
    private final static String API_BASE_URL = "https://kapi.kakao.com";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    public OAuthToken renewToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", refreshToken);

        try {
            OAuthToken response = restTemplate.postForObject(AUTH_BASE_URL + "/oauth/token", body, OAuthToken.class);
            if (response == null) {
                throw new RuntimeException();
            }
            return response;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void unlink(String refreshToken) {
        OAuthToken token = renewToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            KakaoUnlinkResponse response = restTemplate.postForObject(API_BASE_URL + "/v1/user/unlink", entity, KakaoUnlinkResponse.class);
            if (response == null) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
