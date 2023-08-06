package com.web.billim.oauth.dto;

import com.web.billim.member.domain.Member;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.oauth.dto.OAuthLogin;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class OauthMember implements  OAuth2User {

    private final long memberId;
    private final String accountId;
    private final MemberGrade grade;

    public OauthMember (OAuthLogin oAuthLogin, Member member){
        this.memberId = member.getMemberId();
        this.accountId = oAuthLogin.getProviderId();
        this.grade = member.getGrade();
    }

    @Override
    public String getName() {
        return this.accountId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
    public long getMemberId(){
        return this.memberId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(grade);
    }


}
