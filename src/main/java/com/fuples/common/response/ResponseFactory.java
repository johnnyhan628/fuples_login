package com.fuples.common.response;

import com.fuples.common.error.ErrorCode;

public final class ResponseFactory {

    public static <T> DataResponse<T> success(T data) {
        return new DataResponse<>(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), data);
    }

    public static ApiResponse success() {
        return new ApiResponse(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
    }

    public static ApiResponse failure(ErrorCode e) {
        return new ApiResponse(false, e.getCode(), e.getMessage());
    }

    public static ApiResponse failure(ErrorCode e, String message) {
        return new ApiResponse(false, e.getCode(), message);
    }
}

