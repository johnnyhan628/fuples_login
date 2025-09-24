package com.fuples.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    OK("OK", HttpStatus.OK, "success", "성공시 발생"),

    // auth 관련 에러
    AUTH_INVALID_CREDENTIALS("AUTH_INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED, "이메일/비밀번호 불일치", "이메일 또는 비밀번호가 일치하지 않는 경우"),
    AUTH_TOKEN_EXPIRED("AUTH_TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED, "AccessToken 만료", "만료되었거나 유효하지 않은 AccessToken"),
    AUTH_REFRESH_REVOKED("AUTH_REFRESH_REVOKED", HttpStatus.UNAUTHORIZED, "회수/무효화된 Refresh", "회수되었거나 무효화된 RefreshToken"),
    AUTH_FORBIDDEN("AUTH_FORBIDDEN", HttpStatus.FORBIDDEN, "권한 부족", "요청에 필요한 권한이 부족한 경우"),

    // user 관련 에러
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", HttpStatus.CONFLICT, "이메일 중복", "이미 사용 중인 이메일"),

    // 요청 검증 에러
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, "입력값 검증 실패", "요청에 포함된 입력값 검증 실패"),

    INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.", "서버 오류 발생 시"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
    private final String description;
}
