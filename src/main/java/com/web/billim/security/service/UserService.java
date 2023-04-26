package com.web.billim.security.service;


import com.web.billim.member.domain.Member;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.security.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId));

        return User.of(member);
    }

}

