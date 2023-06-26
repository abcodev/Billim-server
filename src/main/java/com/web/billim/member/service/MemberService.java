package com.web.billim.member.service;

import com.web.billim.common.email.dto.EmailAuthRequest;
import com.web.billim.common.email.dto.EmailRequest;
import com.web.billim.common.exception.DuplicatedException;
import com.web.billim.common.email.service.EmailService;
import com.web.billim.common.exception.NotFoundException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.coupon.repository.CouponRepository;
import com.web.billim.coupon.service.CouponService;
import com.web.billim.infra.ImageUploadService;
import com.web.billim.jwt.JwtTokenRedisService;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.TemporaryPasswordDto;
import com.web.billim.member.dto.request.MemberSignupRequest;
import com.web.billim.member.dto.request.UpdateAddressRequest;
import com.web.billim.member.dto.request.UpdateNicknameRequest;
import com.web.billim.member.dto.response.MyPageInfoResponse;
import com.web.billim.member.dto.response.UpdateInfoResponse;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.point.dto.AddPointCommand;
import com.web.billim.point.service.PointService;

import com.web.billim.product.dto.response.ProductListResponse;
import com.web.billim.product.service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final ImageUploadService imageUploadService;
	private final CouponRepository couponRepository;
	private final CouponService couponService;
	private final PointService pointService;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberRedisService memberRedisService;
	private final EmailService emailService;

	public Map<String, String> validateHandling(BindingResult bindingResult) {
		Map<String, String> validatorResult = new HashMap<>();

		for (FieldError error : bindingResult.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}

	@Transactional
	public void signUp(MemberSignupRequest memberSignupRequest) {
		memberSignupRequest.PasswordChange(passwordEncoder);
		Member member = memberRepository.save(memberSignupRequest.toEntity());

		// 쿠폰 주기
		couponRepository.findByName("회원가입 쿠폰")
			.map(coupon -> couponService.issueCoupon(member, coupon))
			.orElseThrow();

		// 포인트 주기
		AddPointCommand command = new AddPointCommand(member, 1000, LocalDateTime.now().plusDays(365));
		pointService.addPoint(command);
	}

	@Transactional
	public boolean checkDuplicateNickname(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	public void certifyEmail(EmailRequest request) {
		validateDuplicated(request.getEmail());
		String authToken = UUID.randomUUID().toString();
		memberRedisService.saveEmailToken(request.getEmail(), authToken);
		emailService.sendMail(request.getEmail(), authToken);
	}

	public void validateDuplicated(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
		}
	}

	public void confirmEmail(EmailAuthRequest emailAuthRequest) {
		if (!memberRedisService.findByEmail(emailAuthRequest.getEmail())
			.equals(emailAuthRequest.getAuthToken())) {
			throw new RuntimeException("인증번호가 일치하지 않습니다.");
		}
	}

	// Domain Service
	public Member retrieve(long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Transactional
	public UpdateInfoResponse retrieveUpdateInfoPage(long memberId) {
		return memberRepository.findById(memberId)
				.map(UpdateInfoResponse::from)
				.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Transactional
	public MyPageInfoResponse retrieveMyPageInfo(long memberId) {
		return memberRepository.findById(memberId).map(member -> {
			long availableAmount = pointService.retrieveAvailablePoint(memberId);
			long availableCouponCount = couponService.retrieveAvailableCouponList(memberId).size();
			return MyPageInfoResponse.of(member, availableAmount, availableCouponCount);
		}).orElseThrow();
	}

	@Transactional
	public void updateProfileImage(long memberId, MultipartFile profileImage) {
		String imageUrl = imageUploadService.upload(profileImage, "profile");
		memberRepository.findById(memberId)
			.ifPresent(member -> {
				member.updateProfileImage(imageUrl);
				memberRepository.save(member);
			});
	}

	@Transactional
	public void updateAddress(long memberId, UpdateAddressRequest updateAddressRequest) {
		memberRepository.findById(memberId)
			.ifPresent(member -> {
				member.updateAddress(updateAddressRequest.getAddress());
				memberRepository.save(member);
			});
	}

	@Transactional
	public void updateNickname(long memberId, UpdateNicknameRequest updateNicknameRequest) {
		if (memberRepository.existsByNickname(updateNicknameRequest.getNickname())) {
			throw new RuntimeException("중복된 닉네임 입니다.");
		}
		memberRepository.findById(memberId)
			.ifPresent(member -> {
				member.updateNickname(updateNicknameRequest.getNickname());
				memberRepository.save(member);
			});
	}

//	@Transactional
//	public void findPassword(TemporaryPasswordDto temporaryPasswordDto) {
//		// 일치하는 회원정보 확인
////		if(memberRepository.existsByEmailAndName(temporaryPasswordDto.getEmail(), temporaryPasswordDto.getName())) {
////			throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
////		}
//		memberRepository.findByEmailAndName(temporaryPasswordDto.getEmail(), temporaryPasswordDto.getName())
//				.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
//
// 		// 임시 비밀번호 전송
//		emailService.sendTempPassword(temporaryPasswordDto);
//
//		// 임시 비밀번호로 비밀번호 저장
//
//	}
}
