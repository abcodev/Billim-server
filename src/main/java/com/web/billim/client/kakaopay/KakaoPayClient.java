package com.web.billim.client.kakaopay;

import com.web.billim.client.iamport.response.IamPortPaymentData;
import com.web.billim.client.iamport.response.IamPortPaymentResponse;
import com.web.billim.client.kakaopay.response.KakaoPayAccessTokenResponse;
import com.web.billim.client.portone.response.PortOneAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    private final RestTemplate restTemplate;

    private final String KAKAOPAY_BASE_URL = "https://open-api.kakaopay.com";

    @Value("${kakaopay.client_id}")
    private String kakaoPayClientId;

    @Value("${kakaopay.client_secret}")
    private String kakaoPayClientSecret;

    @Value("${kakaopay.secret_key}")
    private String kakaoPaySecretKey;

    public String getAccessToken() {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", kakaoPayClientId);
        body.put("client_secret", kakaoPayClientSecret);
        ResponseEntity<KakaoPayAccessTokenResponse> result = restTemplate.postForEntity(KAKAOPAY_BASE_URL + "/oauth/token", body, KakaoPayAccessTokenResponse.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(result.getBody()).getAccessToken();
        }
        throw new RuntimeException("KakaoPay AccessToken 조회에 실패했습니다!");
    }

    public IamPortPaymentData retrievePaymentHistory(String secretKey) {
        String accessToken = this.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<IamPortPaymentResponse> result = restTemplate.exchange(KAKAOPAY_BASE_URL + "/online/v1/payment/ready/" + secretKey, HttpMethod.GET, entity, IamPortPaymentResponse.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(result.getBody()).getResponse();
        }
        throw new RuntimeException("IamPort 결제내역 조회에 실패했습니다!");
    }



}
