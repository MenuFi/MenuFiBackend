package com.menufi.backend.springboot;

public class SuccessResponse<T> extends CustomResponse<T> {

    public SuccessResponse(T data) {
        this(data, null);
    }

    public SuccessResponse(T data, String message) {
        super("success", data, message);
    }
}
