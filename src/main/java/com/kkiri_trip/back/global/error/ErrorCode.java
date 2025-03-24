package com.kkiri_trip.back.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message)
    {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
