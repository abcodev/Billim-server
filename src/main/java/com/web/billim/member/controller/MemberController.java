package com.web.billim.member.controller;

import com.web.billim.common.email.dto.EmailAuthRequest;
import com.web.billim.common.email.dto.EmailRequest;
import com.web.billim.common.validation.CheckIdValidator;
import com.web.billim.common.validation.CheckNickNameValidator;
import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.dto.response.MemberInfoResponse;
import com.web.billim.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkIdValidator);
        binder.addValidators(checkNickNameValidator);
        binder.addValidators(checkPasswordValidator);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> memberSignUp (
            @Valid @RequestBody MemberSignupRequest memberSignupRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
            return new ResponseEntity<>(validatorResult, HttpStatus.OK);
        }
        memberService.signUp(memberSignupRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value ="*이메일인증 링크 발송", notes = "해당 이메일에 인증 링크 발송")
    @ApiImplicitParam(name = "email",dataType = "EmailRequest")
    @PostMapping("/send/email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request){
        memberService.certifyEmail(request);
        return ResponseEntity.ok(200);
    }

    @ApiOperation(value = "*이메일인증 코드 확인", notes = "클라이언트가 링크를 클릭시 해당 APi로 연결")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "인증할 이메일"),
            @ApiImplicitParam(name = "certifyCode", value = "인증번호")})
    @GetMapping("/confirm/email")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailAuthRequest emailAuthRequest){
        memberService.confirmEmail(emailAuthRequest);
        return ResponseEntity.ok(200);
    }

    // 로그아웃



    // 비밀번호 찾기




    // 마이페이지 클릭시 호출 (내 프로필, 정보, 내 쿠폰, 적립금, 리뷰 조회)




    @ApiOperation(value = "*내 회원정보 조회" , notes = "회원 정보 수정 시 내 정보 조회")
    @GetMapping("/my/info")
    public ResponseEntity<MemberInfoResponse> memberInfo(@AuthenticationPrincipal long memberId) {
        Member member = memberService.retrieve(memberId);
        return ResponseEntity.ok(MemberInfoResponse.from(member));
    }

    @ApiOperation(value = "*프로필 이미지 변경", notes = "회원 정보 수정 시 프로필 이미지 변경")
    @PutMapping(value = "/my/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfileImage(@AuthenticationPrincipal long memberId, MultipartFile profileImage) {
        memberService.updateProfileImage(memberId, profileImage);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "주소 변경", notes = "회원 정보 수정 시 주소 변경 저장")
    @PutMapping("/my/address")
    public ResponseEntity<Void> updateAddress(@AuthenticationPrincipal long memberId, @RequestParam String address) {
        memberService.updateAddress(memberId, address);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "닉네임 변경", notes = "회원 정보 수정 시 닉네임 변경 저장")
    @PutMapping("/my/nickname")
    public ResponseEntity<Void> updateNickname(@AuthenticationPrincipal long memberId, @RequestParam String nickname) {
        memberService.updateNickname(memberId, nickname);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "닉네임 중복 확인", notes = "닉네임 중복시 true")
    @GetMapping("/check/nickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(memberService.checkDuplicateNickname(nickname));
    }




    // 비밀번호 재설정
    /*
     * 비밀번호 재설정
     *   1. 기존 패스워드, 변경할 패스워드, 변경할 패스워드 확인을 받는다.
     *   2. 기존 패스워드, 변경할 패스워드를 서버로 전송
     *   3. memberId로 Member 조회
     *   4. Member.getPassword 랑 기존 패스워드 받은거 비교 (BCryp~~ matches()) -> 암호화가 해결됨
     *   	4-1. 대칭키 암호화
     *      4-2. 기존 패스워드를 받음 -> 평문
     *      4-3. 데이터베이스에 있는 패스워드 -> 암호화
     *      4-4. passwordEncoder.matches(평문, 암호화 된거)
     *   5. 변경할 패스워드로 Member 의 password 업데이트.
     */
//    @PutMapping("/my/password")
//    public ResponseEntity<Void> updatePassword(
//            @AuthenticationPrincipal long memberId,
//            @RequestParam String currentPassword, @RequestParam String newPassword
//    ) {
//        memberService.updatePassword(memberId, newPassword);
//        return null;
//    }




    // 소셜 연동



    // 회원 차단



    // 회원 탈퇴


}





