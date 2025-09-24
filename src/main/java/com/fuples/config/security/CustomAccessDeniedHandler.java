package com.fuples.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuples.common.error.ErrorCode;
import com.fuples.common.response.ApiResponse;
import com.fuples.common.response.ResponseFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Spring Security 에서 권한 거부(Access Denied) 시 처리하는 Custom AccessDeniedHandler 구현
 * - 사용자가 접근 권한이 없는 리소스에 접근했을 때 발생하는 예외 처리
 * - 구체적인 권한 제한은 application.yml 의 security 설정을 참고
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Response DTO 객체 생성
        ErrorCode error = ErrorCode.AUTH_FORBIDDEN;
        ApiResponse apiResponse = ResponseFactory.failure(error);

        // json 형태로 변환
        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);

        //응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(error.getHttpStatus().value());
        response.getWriter().write(jsonResponse);
    }
}