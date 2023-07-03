package com.web.billim.security;

import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.security.domain.UserDetailsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetailsEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if(member != null){
            return new UserDetailsEntity(member);
        }
        return null;
    }

    public UserDetailsEntity findByMemberId(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        return new UserDetailsEntity(member);
    }

}
