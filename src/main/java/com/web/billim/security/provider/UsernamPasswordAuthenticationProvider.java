package com.web.billim.security.provider;
import com.web.billim.security.service.UserDetailServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UsernamPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;

    public UsernamPasswordAuthenticationProvider(UserDetailServiceImpl userDetailService,
                                                 PasswordEncoder passwordEncoder
    ) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails user = userDetailService.loadUserByUsername(email);


        if(user != null && this.passwordEncoder.matches(password, user.getPassword())){
            return new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
        }else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
