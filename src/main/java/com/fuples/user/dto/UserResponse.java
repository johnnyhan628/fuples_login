package com.fuples.user.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String email,
        String name,
        List<String> roles
) {
}