package com.web.billim.security.domain;

import com.web.billim.member.domain.Member;
import com.web.billim.member.type.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto implements UserDetails {
    //    @Delegate
    private long memberId;
    private String email;
    private String password;
    private MemberGrade grade;

    public UserDetailsDto(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.grade = member.getGrade();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        // collect.add((GrantedAuthority) () -> grade.toString());

        // Authority : 타입에 대한 제약조건이 있음(제네릭) 컬렉션에 들어갈 수 있는 객체는 GrantAuthority 의 자식이어야 함
        // MemberGrade 자체를 implements GrantAuthority 로 상속받아서도 가능함
        // collection 에 GrantAuthority 를 상속받은 객체들이 들어가야함
        collect.add(new SimpleGrantedAuthority(grade.name()));
        return collect;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
