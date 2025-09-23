package com.fuples.common.response;

import lombok.Getter;

@Getter
public class ApiResponse {

    private final boolean success;

    private final int code;

    private final String message;

    public ApiResponse(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}

