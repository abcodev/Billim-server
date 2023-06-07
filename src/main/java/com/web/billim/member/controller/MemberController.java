package com.web.billim.member.controller;

import com.web.billim.common.dto.EmailAuthRequest;
import com.web.billim.common.dto.EmailRequest;
import com.web.billim.common.validation.CheckIdValidator;
import com.web.billim.common.validation.CheckNickNameValidator;
import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity memberSingUpProc(@Valid @RequestBody MemberSignupRequest memberSignupRequest,
                                   BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
            return new ResponseEntity<>(validatorResult, HttpStatus.OK);
        }
        memberService.signUp(memberSignupRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value ="*이메일인증 링크 발송", notes = "해당 이메일에 인증번호 발송")
    @ApiImplicitParam(name = "email",dataType = "EmailRequest")
    @PostMapping("/send/email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request){
        memberService.certifyEmail(request);
        return ResponseEntity.ok(200);
    }

    @ApiOperation(value = "*이메일인증 코드 확인", notes = "클라이언트가 링크를 클릭시 해당 APi로 연결")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "인증할 이메일"),
            @ApiImplicitParam(name = "certifyCode", value = "인증번호")
    })
    @GetMapping("/confirm/email")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailAuthRequest emailAuthRequest){
        memberService.confirmEmail(emailAuthRequest);
        return ResponseEntity.ok(200);
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


