package com.web.billim.coupon.service;

import com.web.billim.coupon.domain.Coupon;
import com.web.billim.coupon.domain.CouponIssue;
import com.web.billim.coupon.dto.AvailableCouponResponse;
import com.web.billim.coupon.dto.CouponRegisterCommand;
import com.web.billim.coupon.repository.CouponIssueRepository;
import com.web.billim.coupon.repository.CouponRepository;
import com.web.billim.exception.ForbiddenException;
import com.web.billim.exception.NotFoundException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;

    // 0. 새로운 쿠폰 등록
    public Coupon registerNewCoupon(CouponRegisterCommand command) {
        return couponRepository.save(Coupon.from(command));
    }

    // 1. 쿠폰 발급
    public CouponIssue issueCoupon(Member member, Coupon coupon) {
        CouponIssue couponIssue = coupon.issue(member);
        return couponIssueRepository.save(couponIssue);
    }

    // 2. 사용가능한 쿠폰 목록 조회
    @Transactional
    public List<AvailableCouponResponse> retrieveAvailableCouponList(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        return couponIssueRepository.findAllByMember(member).stream()
                .filter(coupon -> LocalDateTime.now().isBefore(coupon.getExpiredAt())) // 만료된거 제외
                .map(AvailableCouponResponse::from) // CouponIssue -> AvailableCouponResponse
                .collect(Collectors.toList());
    }

    // 2. 사용가능한 쿠폰 목록 조회 - 할인율순
    @Transactional
    public List<AvailableCouponResponse> retrieveAvailableCouponListByRate(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        return couponIssueRepository.findAllByMemberOrderByRate(member).stream()
                .filter(coupon -> LocalDateTime.now().isBefore(coupon.getExpiredAt()))
                .map(AvailableCouponResponse::from)
                .collect(Collectors.toList());
    }


    // 3. 쿠폰 사용
    @Transactional
    public void useCoupon(Member member, long couponIssueId) {
        CouponIssue couponIssue = couponIssueRepository.findById(couponIssueId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

        if (!couponIssue.getMember().equals(member)) {
            throw new ForbiddenException(ErrorCode.MISMATCH_MEMBER);
        }
        couponIssue.use();
    }

    @Transactional
	public void refund(CouponIssue couponIssue) {
        Optional.ofNullable(couponIssue)
                .ifPresent(CouponIssue::available);
	}

}
