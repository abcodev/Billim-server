//package com.web.billim.security.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//public class CustomRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
//
//    private Map<String, OAuth2AuthorizationRequest> repository;
//
//    @Override
//    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
//        return repository.get("TEST");
//    }
//
//    @Override
//    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
//        repository.put("TEST", authorizationRequest);
//    }
//
//    @Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
//        return repository.get("TEST");
//    }
//}
