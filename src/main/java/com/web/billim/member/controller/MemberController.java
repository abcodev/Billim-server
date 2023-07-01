package com.web.billim.member.controller;

import com.web.billim.common.email.dto.EmailAuthRequest;
import com.web.billim.common.email.dto.EmailRequest;
import com.web.billim.common.validation.CheckIdValidator;
import com.web.billim.common.validation.CheckNickNameValidator;
import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.dto.FindPasswordRequest;
import com.web.billim.member.dto.request.*;
import com.web.billim.member.dto.response.MyPageInfoResponse;
import com.web.billim.member.dto.response.UpdateInfoResponse;
import com.web.billim.member.service.MemberService;
import com.web.billim.product.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CheckIdValidator checkIdValidator;
    private final CheckNickNameValidator checkNickNameValidator;
    private final CheckPasswordValidator checkPasswordValidator;
    private final ReviewService reviewService;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkIdValidator);
        binder.addValidators(checkNickNameValidator);
        binder.addValidators(checkPasswordValidator);
    }

    @ApiOperation(value = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> memberSignUp (
            @Valid @RequestBody MemberSignupRequest memberSignupRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
            return new ResponseEntity<>(validatorResult, HttpStatus.BAD_REQUEST);
        }
        memberService.signUp(memberSignupRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "닉네임 중복 확인", notes = "true 닉네임 중복")
    @GetMapping("/check/nickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(memberService.checkDuplicateNickname(nickname));
    }

    @ApiOperation(value ="이메일인증 링크 발송", notes = "해당 이메일에 인증 링크 발송")
    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest request) {
        memberService.certifyEmail(request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "이메일인증 코드 확인", notes = "클라이언트가 링크를 클릭시 해당 APi로 연결")
    @PostMapping("/email/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestBody EmailAuthRequest emailAuthRequest) {
        memberService.confirmEmail(emailAuthRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "비밀번호 찾기", notes = "이메일, 이름 입력시 해당 이메일로 임시 비밀번호 전송")
    @PostMapping("/email/find/password")
    public ResponseEntity<Void> findPassword(@RequestBody FindPasswordRequest req) {
        memberService.findPassword(req);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "*마이페이지 헤더 정보 조회", notes = "내 프로필, 쿠폰, 적립금, 작성가능한 리뷰 조회")
    @GetMapping("/my/page")
    public ResponseEntity<MyPageInfoResponse> myPageInfo(@AuthenticationPrincipal long memberId) {
        MyPageInfoResponse resp = memberService.retrieveMyPageInfo(memberId);
        long availableReview = reviewService.myReviewNoCount(memberId);
        resp.setAvailableReview(availableReview);
        return ResponseEntity.ok(resp);
    }

    @ApiOperation(value = "내 회원정보 조회" , notes = "회원 정보 수정 시 내 정보 조회")
    @GetMapping("/my/info")
    public ResponseEntity<UpdateInfoResponse> updateMemberInfo(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok(memberService.retrieveUpdateInfoPage(memberId));
    }

    @ApiOperation(value = "회원정보 프로필 이미지 변경", notes = "회원 정보 수정 시 프로필 이미지 변경")
    @PutMapping(value = "/my/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfileImage(
            @AuthenticationPrincipal long memberId,
            MultipartFile profileImage
    ) {
        memberService.updateProfileImage(memberId, profileImage);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원정보 주소 변경", notes = "회원 정보 수정 시 주소 변경 저장")
    @PutMapping("/my/address")
    public ResponseEntity<Void> updateAddress(
            @AuthenticationPrincipal long memberId,
            @RequestBody UpdateAddressRequest updateAddressRequest
    ) {
        memberService.updateAddress(memberId, updateAddressRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원정보 닉네임 변경", notes = "회원 정보 수정 시 닉네임 변경 저장")
    @PutMapping("/my/nickname")
    public ResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal long memberId,
            @RequestBody UpdateNicknameRequest updateNicknameRequest
    ) {
        memberService.updateNickname(memberId, updateNicknameRequest);
        return ResponseEntity.ok().build();
    }

    // 비밀번호 재설정
    @PutMapping("/my/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok().build();
    }


    // 소셜 연동



    // 회원 차단



    // 회원 탈퇴


}





