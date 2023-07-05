package com.web.billim.member.controller;

import com.web.billim.common.validation.CheckIdValidator;
import com.web.billim.common.validation.CheckNickNameValidator;
import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.dto.request.FindPasswordRequest;
import com.web.billim.member.dto.UpdatePasswordCommand;
import com.web.billim.member.dto.request.*;
import com.web.billim.member.dto.response.MyPageInfoResponse;
import com.web.billim.member.dto.response.UpdateInfoResponse;
import com.web.billim.member.service.MemberService;
import com.web.billim.review.service.ReviewService;
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

    @ApiOperation(value = "비밀번호 찾기", notes = "이메일, 이름 입력시 해당 이메일로 임시 비밀번호 전송")
    @PostMapping("/find/password")
    public ResponseEntity<Void> findPassword(@RequestBody FindPasswordRequest req) {
        memberService.findPassword(req);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "마이페이지 헤더 정보 조회", notes = "내 프로필, 쿠폰, 적립금, 작성가능한 리뷰 조회")
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

    @ApiOperation(value = "회원정보 주소 변경", notes = "회원 정보 수정 시 주소 변경")
    @PutMapping("/my/address")
    public ResponseEntity<Void> updateAddress(
            @AuthenticationPrincipal long memberId,
            @RequestBody UpdateAddressRequest updateAddressRequest
    ) {
        memberService.updateAddress(memberId, updateAddressRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원정보 닉네임 변경", notes = "회원 정보 수정 시 닉네임 변경")
    @PutMapping("/my/nickname")
    public ResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal long memberId,
            @RequestBody UpdateNicknameRequest updateNicknameRequest
    ) {
        memberService.updateNickname(memberId, updateNicknameRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "비밀번호 재설정", notes = "회원 정보 수정 시 비밀번호 재설정")
    @PutMapping("/my/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal long memberId,
            @RequestBody UpdatePasswordRequest req
    ) {
        UpdatePasswordCommand command = new UpdatePasswordCommand(memberId, req);
        memberService.updatePassword(command);
        return ResponseEntity.ok().build();
    }


    // 소셜 연동



    // 회원 차단



    // 회원 탈퇴


}





