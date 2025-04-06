package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import lombok.Getter;

@Getter
public class ScheduleException extends RuntimeException{
    private final ScheduleErrorCode errorCode;

    public ScheduleException(ScheduleErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
