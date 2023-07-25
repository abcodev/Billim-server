package com.web.billim.point.dto;

import com.web.billim.member.domain.Member;
import com.web.billim.payment.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPointCommand {

    private Member member;
    private long amount;
    private LocalDateTime expiredAt;

}
