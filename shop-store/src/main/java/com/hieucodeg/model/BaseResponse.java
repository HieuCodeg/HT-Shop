package com.hieucodeg.model;


public class BaseResponse<T> {
    int statusCode;
    String message;
    T data;

    public int getStatusCode() {
        return statusCode;
    }

    public BaseResponse<T> getValidResponse(int StatusCode, String message, T data) {
        BaseResponse<T> baseResponse = new BaseResponse<T>();
        baseResponse.statusCode = StatusCode;
        baseResponse.message = message;
        baseResponse.data = data;
        return baseResponse;
    }

    public BaseResponse<T> getErrorResponse(int StatusCode, String message) {
        BaseResponse<T> baseResponse = new BaseResponse<T>();
        baseResponse.statusCode = StatusCode;
        baseResponse.message = message;
        return baseResponse;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
