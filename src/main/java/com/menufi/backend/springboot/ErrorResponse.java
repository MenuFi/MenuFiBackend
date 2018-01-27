package com.menufi.backend.springboot;

public class ErrorResponse<T> extends CustomResponse<T> {

    public ErrorResponse(T data) {
        this(data, null);
    }

    public ErrorResponse(Exception e) {
        this(null, e.getMessage());
    }

    public ErrorResponse(T data, String message) {
        super("error", data, message);
    }
}
