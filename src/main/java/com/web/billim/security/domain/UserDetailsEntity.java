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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsEntity implements UserDetails {
//    @Delegate
    private long memberId;
    private String email;

    private String password;
    private List<MemberGrade> grade;

    public UserDetailsEntity(Member member){
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.grade = List.of(member.getGrade());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return this.grade;
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
