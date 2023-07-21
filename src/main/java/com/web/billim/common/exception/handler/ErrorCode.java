package com.web.billim.common.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALIDATION_INPUT(BAD_REQUEST, "잘못된 사용자 입력입니다"),


    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */

    /* refresh token */
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),

    /* jwt common */
    EXPIRED_TOKEN(UNAUTHORIZED,"액세스 토큰은 만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(UNAUTHORIZED,"변조된 토큰입니다."),
    INVALID_TOKEN(UNAUTHORIZED,"유효하지 않은 토큰입니다"),
    WRONG_TYPE_TOKEN(UNAUTHORIZED,"변조된 토큰입니다."),
    UNKNOWN_ERROR(UNAUTHORIZED,"토큰이 존재하지 않습니다."),


    /* 아이디 및 비밀번호 일치 하지 않음*/
    MISMATCH_PASSWORD(UNAUTHORIZED,"아이디 또는 비밀번호가 일치하지 않습니다."),



//
//    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "잘못된 액세스 토큰입니다."),
//    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
//    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),


    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 사용자 정보를 찾을 수 없습니다"),
    PRODUCT_NOT_FOUND(NOT_FOUND, "상품정보를 찾을 수 없습니다"),
    ORDER_NOT_FOUND(NOT_FOUND,"주문정보를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND, "상품 카테고리 정보를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    RESOURCE_NOT_FOUND(NOT_FOUND, "리소스를 찾을 수 없습니다."),

    
    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),
    DUPLICATE_EMAIL(CONFLICT, "중복된 이메일 입니다."),
    DUPLICATE_NICKNAME(CONFLICT, "중복된 닉네임 입니다."),

    /* INTERNAL_SERVER_ERROR */
    IMAGE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    EMAIL_SEND_FAILED(INTERNAL_SERVER_ERROR,"이메일 전송에 실패하였습니다."),
    ORDER_DUPLICATED_REQUEST(INTERNAL_SERVER_ERROR, "해당 사용자가 이미 주문중인 거래가 있습니다."),
    ORDER_DUPLICATED_PERIOD(INTERNAL_SERVER_ERROR, "해당 제품은 다른 사용자가 거래중입니다.");


    private final HttpStatus httpStatus;
    private final String message;

}
