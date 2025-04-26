package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.FeedUserErrorCode;
import lombok.Getter;

@Getter
public class FeedUserException extends RuntimeException {
    private final FeedUserErrorCode errorCode;

    public FeedUserException(FeedUserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}