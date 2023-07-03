package com.web.billim.security;
import com.web.billim.common.exception.UnAuthorizedException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.security.domain.UserDetailsEntity;
import com.web.billim.security.dto.LoginAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsernamPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;

    public UsernamPasswordAuthenticationProvider(UserDetailServiceImpl userDetailService,
                                                 PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetailsEntity user = userDetailService.loadUserByUsername(email);
        if(user != null && this.passwordEncoder.matches(password, user.getPassword())){
            return new LoginAuthenticationToken(user.getAuthorities(),user.getMemberId());
        }else {
            throw new UnAuthorizedException(ErrorCode.MISMATCH_PASSWORD);
        }
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
