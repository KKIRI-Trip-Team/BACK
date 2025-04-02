package com.kkiri_trip.back.global.error.errorcode;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    HttpStatus getHttpStatus();
    String getErrorCode();
    String getMessage();
}
