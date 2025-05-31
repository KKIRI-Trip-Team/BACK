package com.kkiri_trip.back.global.error.errorcode;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements BaseErrorCode {

    INVALID_DAYNUMBER(HttpStatus.BAD_REQUEST, "SCHEDULE_001", "일차는 0이하일 수 없습니다."),
    INVALID_FEED(HttpStatus.BAD_REQUEST, "SCHEDULE_002", "유효하지 않는 피드입니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_002", "해당 스케줄을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
