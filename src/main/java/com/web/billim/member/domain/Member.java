package com.web.billim.member.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.type.MemberGrade;
import lombok.*;

import javax.persistence.*;


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
    private long memberId;
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
        this.profileImageUrl = this.profileImageUrl == null ? "https://cdn-icons-png.flaticon.com/512/8246/8246830.png": this.profileImageUrl;
    }
}
