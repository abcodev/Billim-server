package com.web.billim.member.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.exception.UnAuthorizedException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.type.MemberGrade;

import com.web.billim.member.type.MemberType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member")
@Builder
@Getter
@Setter
public class Member extends JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String address;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    private String profileImageUrl = DEFAULT_IMAGE_URL;
    private String useYn;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    public void updateInfo(String imageUrl, String nickname, String address) {
        if (imageUrl != null) {
            this.profileImageUrl = imageUrl;
        }
        this.nickname = nickname;
        this.address = address;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public Member unregister() {
        this.useYn = "N";
        return this;
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new UnAuthorizedException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public boolean isDefaultImage() {
        return this.profileImageUrl.equals(DEFAULT_IMAGE_URL);
    }

    private final static String DEFAULT_IMAGE_URL = "https://s3-billim.s3.ap-northeast-2.amazonaws.com/profile/profile-default.png";
}
