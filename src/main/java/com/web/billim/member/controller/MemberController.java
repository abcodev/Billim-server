package com.web.billim.member.controller;

import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.request.FindIdRequest;
import com.web.billim.member.dto.request.MemberSignupRequest;
//import com.web.billim.member.dto.response.FindIdResponse;
import com.web.billim.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
//    private final CheckIdValidator checkIdValidator;
//    private final CheckNickNameValidator checkNickNameValidator;
    private final CheckPasswordValidator checkPasswordValidator;

//    @InitBinder
//    public void validatorBinder(WebDataBinder binder) {
//        binder.addValidators(checkIdValidator);
//        binder.addValidators(checkNickNameValidator);
//        binder.addValidators(checkPasswordValidator);
//    }

    @PostMapping("/signup")
    public ResponseEntity memberSingUpProc(@Valid @RequestBody MemberSignupRequest memberSignupRequest,
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
//    @GetMapping("/member/logout")
//    public ResponseEntity<Void> logoutPage(HttpServletRequest request, HttpServletResponse response) {
//        new SecurityContextLogoutHandler().logout(request, response,
//                SecurityContextHolder.getContext().getAuthentication());
//        return ResponseEntity.ok().build();
//    }

    // 아이디 찾기
//    @PostMapping("/member/findId")
//    public ResponseEntity<FindIdResponse> findId(FindIdRequest findIdRequest) {
//        return ResponseEntity.ok(memberService.findId(findIdRequest));
//    }

    // 비밀번호 찾기


    // 프로필 이미지 변경


    // 비밀번호 재설정


    // 닉네임 변경


    // 주소 변경
    @PutMapping("/change/Address")
    public ResponseEntity<Void> changeAddress() {
        return ResponseEntity.ok().build();
    }

    // 소셜 연동

    // 회원 차단

    // 회원 탈퇴


}


/*
 * 비밀번호 재설정
 *   1. 기존 패스워드, 변경할 패스워드, 변경할 패스워드 확인을 받는다.
 *   2. 이메일, 기존 패스워드, 변경할 패스워드를 서버로 전송
 *   3. 이메일로 Member 조회
 *   4. Member.getPassword 랑 기존 패스워드 받은거 비교 (BCryp~~ matches()) -> 암호화가 해결됨
 *   	4-1. 대칭키 암호화
 *      4-2. 기존 패스워드를 받음 -> 평문
 *      4-3. 데이터베이스에 있는 패스워드 -> 암호화
 *      4-4. passwordEncoder.matches(평문, 암호화 된거)
 *   5. 변경할 패스워드로 Member 의 password 업데이트.
 */


