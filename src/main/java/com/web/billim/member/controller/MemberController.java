package com.web.billim.member.controller;

import com.web.billim.common.validation.CheckIdValidator;
import com.web.billim.common.validation.CheckNickNameValidator;
import com.web.billim.common.validation.CheckPasswordValidator;
import com.web.billim.member.dto.request.FindPasswordRequest;
import com.web.billim.member.dto.command.UpdatePasswordCommand;
import com.web.billim.member.dto.request.*;
import com.web.billim.member.dto.response.HeaderInfoResponse;
import com.web.billim.member.dto.response.MyPageInfoResponse;
import com.web.billim.member.dto.response.MemberInfoResponse;
import com.web.billim.member.service.MemberService;
import com.web.billim.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@Tag(name = "회원", description = "MemberController")
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

    @Operation(summary = "회원가입")
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

    @Operation(summary = "닉네임 중복 확인", description = "true 시 중복된 닉네임")
    @GetMapping("/check/nickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(memberService.checkDuplicateNickname(nickname));
    }

    @Operation(summary = "비밀번호 찾기", description = "이메일, 이름 입력시 해당 이메일로 임시 비밀번호 전송")
    @PostMapping("/find/password")
    public ResponseEntity<Void> findPassword(@RequestBody FindPasswordRequest req) {
        memberService.findPassword(req);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "마이페이지 헤더 정보 조회", description = "내 프로필, 쿠폰, 적립금, 작성가능한 리뷰 조회")
    @GetMapping("/my-page")
    public ResponseEntity<MyPageInfoResponse> myPageInfo(@AuthenticationPrincipal long memberId) {
        MyPageInfoResponse resp = memberService.retrieveMyPageInfo(memberId);
        long availableReview = reviewService.writableReviewCount(memberId);
        resp.setAvailableReview(availableReview);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "내 회원정보 조회" , description = "회원 정보 수정 시 내 정보 조회한다.")
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponse> memberInfo(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok(memberService.retrieveUpdateInfoPage(memberId));
    }

    @Operation(summary = "회원 정보 수정" , description = "회원 정보 수정된 정보를 저장한다.")
    @PutMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberInfoResponse> updateInfo(
            @AuthenticationPrincipal long memberId,
            @ModelAttribute MemberInfoUpdateRequest req
    ) {
        memberService.updateInfo(memberId, req);
        return ResponseEntity.ok(memberService.retrieveUpdateInfoPage(memberId));
    }

    @Operation(summary = "비밀번호 재설정", description = "회원 정보 수정 시 비밀번호 재설정")
    @PutMapping("/info/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal long memberId,
            @RequestBody UpdatePasswordRequest req
    ) {
        UpdatePasswordCommand command = new UpdatePasswordCommand(memberId, req);
        memberService.updatePassword(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메인페이지 헤더 조회", description = "메인페이지 헤더 사용자 프로필 이미지를 조회한다.")
    @GetMapping("/header")
    public ResponseEntity<HeaderInfoResponse> header(@AuthenticationPrincipal long memberId) {
        return ResponseEntity.ok(memberService.retrieveHeaderInfo(memberId));
    }

    @Operation(summary = "회원 탈퇴", description = "비밀번호를 RequestBody 에 넣어주세요.")
    @PostMapping("/unregister")
    public void unregister(@RequestBody Map<String, String> passwordMap,
                            @AuthenticationPrincipal long memberId ){
        memberService.unregister(memberId, passwordMap.get("password"));
    }

//    @GetMapping("/projectiontest/test")  -- postMan test
//    @Scheduled(fixedRate = 180000) // 3분마다 실행
    @Scheduled(cron = "0 0 0 1 * ?")  //-- 실제 구동 시간 / 매월 1일 오전 12시
    public void memberGradeScheduler(){
        log.info(String.format("[PointController] savingPointScheduler Action! (Time: %s)", LocalDateTime.now()));
        log.info("test 시작");
        memberService.memberGradeCheck();
    }

}





