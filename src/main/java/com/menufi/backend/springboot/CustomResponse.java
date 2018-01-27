package com.menufi.backend.springboot;

public class CustomResponse<T> {
    private String status;
    private T data;
    private String message;

    public CustomResponse(String status) {
        this(status, null, null);
    }

    public CustomResponse(String status, String message) {
        this(status, null, message);
    }

    public CustomResponse(String status, T data) {
        this(status, data, null);
    }

    public CustomResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
