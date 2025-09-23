package com.fuples.common.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super("code: " + errorCode.getCode() + ", message: " + errorCode.getMessage());
        this.errorCode = errorCode;
    }
}