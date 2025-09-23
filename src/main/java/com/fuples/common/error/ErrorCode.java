package com.fuples.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(0, HttpStatus.OK, "success", "성공시 발생"),

    // 공통 에러
    BAD_PARAMETER(-1, HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다.", "올바르지 않은 요청입니다."),
    UNAUTHORIZED_ACCESS_TOKEN(-2, HttpStatus.UNAUTHORIZED, "잘못된 access token 입니다.", "잘못된 액세스 토큰인 경우 (토큰이 없거나, 만료된 경우를 포함)"),
    NO_PERMISSION(-3, HttpStatus.FORBIDDEN, "이 리소스에 접근할 권한이 없습니다.", "해당 리소스에 접근할 수 있는 권한이 아닌 경우"),
    INTERNAL_ERROR(-4, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.", "서버 오류 발생 시"),

    //auth(-10 ~)
    UNAUTHORIZED_REFRESH_TOKEN(-10, HttpStatus.UNAUTHORIZED, "잘못된 refresh token 입니다.", "잘못된 리프레시 토큰인 경우 (토큰이 없거나, 만료된 경우를 포함)"),
    UNREACHABLE_KAKAO_TOKEN(-11, HttpStatus.UNAUTHORIZED, "access token이 존재하지 않습니다.", "카카오 제공 데이터에서 엑세스 토큰이 존재하지 않는 경우"),
    NO_REFRESH_TOKEN(-12, HttpStatus.UNAUTHORIZED, "refresh token이 존재하지 않습니다.", "DB에 refresh token이 존재하지 않는 경우"),
    //user(-20 ~)
    NO_USER(-20, HttpStatus.BAD_REQUEST, "존재하지 않는 계정입니다.", "존재하지 않는 계정"),

    //bible(-30 ~)
    INVALID_BIBLE_ID(-30, HttpStatus.BAD_REQUEST, "잘못된 성경 ID 입니다.", "성경 고유키 잘못됨"),

    //bible-questions (-40 ~)
    DUPLICATED_BIBLE_QUESTION(-40, HttpStatus.BAD_REQUEST, "이미 질문한 성경 구절입니다.", "이미 질문을 올린 성경 구절"),
    CANNOT_MODIFY_BIBLE_QUESTION(-41, HttpStatus.BAD_REQUEST, "이미 답변이 달린 질문은 변경이 불가능합니다.", "목사님 답변이 달린 경우 질문 수정/삭제 불가"),
    ALREADY_REPORTED_QUESTION(-42, HttpStatus.BAD_REQUEST, "이미 신고 처리된 질문입니다.", "이미 신고 처리된 경우, 수정/삭제 불가"),
    INVALID_QUESTION_ID(-43, HttpStatus.BAD_REQUEST, "잘못된 질문 ID 입니다.", "존재하지 않는 질문 ID인 경우. 또는 해당 유저의 질문 ID가 아닌 경우"),
    ALREADY_DELETED_QUESTION(-44, HttpStatus.BAD_REQUEST, "이미 삭제 처리된 질문입니다.", "이미 삭제 처리된 질문"),

    //highlight(-50 ~)
    INVALID_HIGHLIGHT_ID(-50, HttpStatus.BAD_REQUEST, "삭제할 수 없는 하이라이트입니다.", "하이라이트 ID 잘못됨 또는 해당 유저의 하이라이트가 아님"),
    DUPLICATE_HIGHLIGHT(-51, HttpStatus.BAD_REQUEST, "이미 하이라이트 처리 되었습니다.", "이미 하이라이트 처리 함"),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
    private final String description;
}
