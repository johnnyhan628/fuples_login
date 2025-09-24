package com.fuples.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuples.common.error.ErrorCode;
import com.fuples.common.response.ApiResponse;
import com.fuples.common.response.ResponseFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Spring Security 에서 인증 실패 시 처리하는 Custom AuthenticationEntryPoint 구현
 * - 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출되어 인증 오류를 처리
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Response DTO 객체 생성
        ErrorCode error = resolveErrorCode(authException);
        ApiResponse apiResponse = ResponseFactory.failure(error);

        // json 형태로 변환
        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);

        //응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(error.getHttpStatus().value());
        response.getWriter().write(jsonResponse);
    }

    private ErrorCode resolveErrorCode(AuthenticationException authException) {
        if (authException instanceof CredentialsExpiredException || authException instanceof InsufficientAuthenticationException) {
            return ErrorCode.AUTH_TOKEN_EXPIRED;
        }

        if (authException instanceof BadCredentialsException) {
            return ErrorCode.AUTH_INVALID_CREDENTIALS;
        }

        return ErrorCode.AUTH_INVALID_CREDENTIALS;
    }
}
