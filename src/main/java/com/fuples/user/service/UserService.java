package com.fuples.user.service;

import com.fuples.common.error.CustomException;
import com.fuples.common.error.ErrorCode;
import com.fuples.user.dto.UserResponse;
import com.fuples.user.entity.User;
import com.fuples.user.repo.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED));

        List<String> roles = List.of(user.getRole());
        return new UserResponse(user.getUserId(), user.getEmail(), user.getName(), roles);
    }
}