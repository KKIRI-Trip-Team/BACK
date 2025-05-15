package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.DashboardErrorCode;
import lombok.Getter;

@Getter
public class DashboardException extends RuntimeException {
    private final DashboardErrorCode errorCode;

    public DashboardException(DashboardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}