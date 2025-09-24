package com.fuples.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fuples.common.error.CustomException;
import com.fuples.common.error.ErrorCode;
import com.fuples.user.entity.User;
import com.fuples.user.repo.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(username)).orElseThrow(() -> new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED));
        return new CustomUserDetails(user);
    }


}
