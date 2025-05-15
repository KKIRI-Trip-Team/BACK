package com.kkiri_trip.back.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DashboardErrorCode implements BaseErrorCode {
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "DASHBOARD_001", "이미 존재하는 대시보드 입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "DASHBOARD_002", "대시보드가 존재하지 않습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}