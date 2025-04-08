package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.ScheduleItemErrorCode;
import lombok.Getter;

@Getter
public class ScheduleItemException extends RuntimeException{
    private final ScheduleItemErrorCode errorCode;

    public ScheduleItemException(ScheduleItemErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}