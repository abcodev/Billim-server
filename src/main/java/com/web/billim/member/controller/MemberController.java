package com.web.billim.member.controller;

import com.web.billim.common.validation.CheckIdValidator;
import com.web.billim.common.validation.CheckNickNameValidator;
import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.dto.request.FindIdRequest;
import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.dto.response.FindIdResponse;
import com.web.billim.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
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

//    @PostMapping("/member/signup")
//    public String memberSingUpProc(@Valid MemberSignupRequest memberSignupRequest,
//                                   BindingResult bindingResult,
//                                   Model model
//    ) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("memberDto", memberSignupRequest);
//
//            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);
//            for (String key : validatorResult.keySet()) {
//                model.addAttribute(key, validatorResult.get(key));
//            }
//            return "pages/member/signup";
//        }
//        memberService.singUp(memberSignupRequest);
//        return "pages/home";
//    }

    @GetMapping("/member/logout")
    public ResponseEntity<Void> logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/member/findId")
//    public ResponseEntity<FindIdResponse> findId(FindIdRequest findIdRequest) {
//        return ResponseEntity.ok(memberService.findId(findIdRequest));
//    }

}



/*
 *  [ 비밀번호 재설정 ]
 *   1. 사용자에게 아이디/이메일 인증을 받는다.
 *   2. 기존 패스워드, 변경할 패스워드, 변경할 패스워드 확인을 받는다.
 *   3. 아이디, 기존 패스워드, 변경할 패스워드를 서버로 전송
 *   4. 아이디로 Member 조회
 *   5. Member.getPassword 랑 기존 패스워드 받은거 비교 (BCryp~~ matches()) -> 암호화가 해결됨
 *   	5-1. 대칭키 암호화
 *      5-2. 기존 패스워드를 받음 -> 평문
 *      5-3. 데이터베이스에 있는 패스워드 -> 암호화
 *      5-4. passwordEncoder.matches(평문, 암호화 된거)
 *   6. 변경할 패스워드로 Member 의 password 업데이트.
 */


