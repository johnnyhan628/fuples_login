package com.fuples.user.controller;

import com.fuples.common.error.CustomException;
import com.fuples.common.error.ErrorCode;
import com.fuples.config.security.CustomUserDetails;
import com.fuples.user.dto.UserResponse;
import com.fuples.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }
        UserResponse response = userService.getMe(userDetails.getUserId());
        return ResponseEntity.ok(response);
    }
}