package com.fuples.common.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends ApiResponse {
    private final T data;

    public DataResponse(boolean success, String code, String message, T data) {
        super(success, code, message);
        this.data = data;
    }
}
