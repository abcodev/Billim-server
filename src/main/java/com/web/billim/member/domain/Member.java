package com.web.billim.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.type.MemberGrade;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member")
@Builder
@Getter
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

    private String profileImageUrl;

    @PrePersist
    public void prePersist(){
        this.profileImageUrl = this.profileImageUrl == null ? "https://billim.s3.ap-northeast-2.amazonaws.com/profile/profile-default.png": this.profileImageUrl;
    }

//    public void updateProfileImage(String imageUrl) {
//        this.profileImageUrl = imageUrl;
//    }

//    public void updateAddress(String address) {
//        this.address = address;
//    }

//    public void updateNickname(String nickname) {
//        this.nickname = nickname;
//    }

    public void updateInfo(String imageUrl, String address, String nickname) {
        if (imageUrl != null) {
            this.profileImageUrl = imageUrl;
        }
        this.address = address;
        this.nickname = nickname;
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
