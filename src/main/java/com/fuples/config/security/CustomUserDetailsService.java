package com.fuples.config.security;

import com.fuples.common.error.CustomException;
import com.fuples.common.error.ErrorCode;
import com.fuples.user.entity.User;
import com.fuples.user.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Long userId = Long.valueOf(username);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED));
            return new CustomUserDetails(user);
        } catch (NumberFormatException ex) {
            throw new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }
    }
}