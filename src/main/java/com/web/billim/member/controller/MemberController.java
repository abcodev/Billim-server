package com.web.billim.member.controller;

import com.web.billim.common.validation.CheckIdValidator;
import com.web.billim.common.validation.CheckNickNameValidator;
import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.dto.response.MemberInfoResponse;
import com.web.billim.member.service.MemberService;
import com.web.billim.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
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
    public ResponseEntity memberSignUpProc(@Valid @RequestBody MemberSignupRequest memberSignupRequest,
                                           BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사에서 걸린 경우
            // 2. 어디가 잘 못 됬는지 알려준다.
            // valid_~ 형태의 String 으로 반환해줌
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
            return new ResponseEntity<>(validatorResult, HttpStatus.OK);
        }
        memberService.signUp(memberSignupRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 로그아웃


    // 아이디 찾기
//    @PostMapping("/member/findId")
//    public ResponseEntity<FindIdResponse> findId(FindIdRequest findIdRequest) {
//        return ResponseEntity.ok(memberService.findId(findIdRequest));
//    }


    // 비밀번호 찾기


    // 회원정보 전체 불러오기
//    @GetMapping("/my/info")
//    public ResponseEntity<List<MemberInfoResponse>> memberInfo(@AuthenticationPrincipal long memberId) {
//        List<MemberInfoResponse> infoList = memberService.findMemberInfo(memberId);
//        return ResponseEntity.ok(infoList);
//    }


    // 프로필 사진 변경
    @PutMapping(value = "/my/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfileImage(long memberId, MultipartFile profileImage) {
        memberService.updateProfileImage(memberId, profileImage);
        return ResponseEntity.ok().build();
    }


    // 주소 변경
//    @PutMapping("/my/address")
//    public ResponseEntity<Void> updateAddress(long memberId, String address) {
//        memberService.updateAddress(memberId, address);
//        return ResponseEntity.ok().build();
//    }

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
//    public ResponseEntity<Void> updatePassword(long memberId, String currentPassword, String newPassword) {
//        memberService.updatePassword(memberId, newPassword);
//        return null;
//    }


    // 닉네임 변경
//    @PutMapping("/my/nickname")
//    public ResponseEntity<Void> updateNickname(long memberId, String nickname) {
//        memberService.updateNickname(memberId, nickname);
//        return ResponseEntity.ok().build();
//    }


    // 소셜 연동

    // 회원 차단

    // 회원 탈퇴


}





