package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}