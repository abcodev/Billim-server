package com.web.billim.client.portone.vo;

import com.web.billim.client.iamport.response.*;
import com.web.billim.client.portone.response.PortOneAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortOneClient {

    private final RestTemplate restTemplate;
    private final String PORT_ONE_BASE_URL = "https://api.portone.io";

    @Value("${port_one.secret}")
    private String portOneSecret;

    public String getAccessToken() {
        Map<String, String> body = new HashMap<>();
        body.put("apiSecret", portOneSecret);
        ResponseEntity<PortOneAccessTokenResponse> result = restTemplate.postForEntity(PORT_ONE_BASE_URL + "/login/api-secret", body, PortOneAccessTokenResponse.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(result.getBody()).getAccessToken();
        }
        throw new RuntimeException("PortOne AccessToken 조회에 실패했습니다!");
    }

    public IamPortPaymentData retrievePaymentHistory(String paymentId) {
        String accessToken = this.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<IamPortPaymentResponse> result = restTemplate.exchange(PORT_ONE_BASE_URL + "/payments/" + paymentId, HttpMethod.GET, entity, IamPortPaymentResponse.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(result.getBody()).getResponse();
        }
        throw new RuntimeException("PortOne 결제내역 조회에 실패했습니다!");
    }

    public void cancel(String impUid) {
        String accessToken = this.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imp_uid", impUid);

        ResponseEntity<IamPortResponse<IamPortPaymentCancelResponse>> resp = restTemplate.exchange(
                PORT_ONE_BASE_URL + "/payments/cancel",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                new ParameterizedTypeReference<>() { }
        );
        if (resp.getStatusCode().isError()) {
            throw new RuntimeException("결제 취소 실패!");
        } else if (resp.getStatusCode().is2xxSuccessful()) {
            System.out.println(resp.getBody());
        }
    }


}
