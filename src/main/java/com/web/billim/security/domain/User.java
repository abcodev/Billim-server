package com.web.billim.security.domain;

import com.web.billim.member.domain.Member;
import com.web.billim.member.type.MemberGrade;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
//@AllArgsConstructor
public class User implements UserDetails {

//    private final Member member;

    private final int memberId;
    private final String userId;
    private final String password;
    private final String name;
    private final String nickname;
    private final String address;
    private final String email;
    private final MemberGrade grade;
    private final String profileImageUrl;
    private final LocalDateTime createdAt;

    public static User of(Member member) {
        return User.builder()
                .memberId(member.getMemberId())
                .userId(member.getUserId())
                .password(member.getPassword())
                .name(member.getName())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .email(member.getEmail())
                .grade(member.getGrade())
                .profileImageUrl(member.getProfileImageUrl())
                .createdAt(member.getCreatedAt())
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
