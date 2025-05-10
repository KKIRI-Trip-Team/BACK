package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import lombok.Getter;

@Getter
public class FeedException extends RuntimeException {
    private final FeedErrorCode errorCode;

    public FeedException(FeedErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}