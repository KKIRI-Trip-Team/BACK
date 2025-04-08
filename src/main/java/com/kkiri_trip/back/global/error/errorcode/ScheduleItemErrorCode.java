package com.kkiri_trip.back.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleItemErrorCode implements BaseErrorCode {
    INVALID_ITEMORDER(HttpStatus.BAD_REQUEST, "SCHEDULEITEM_001", "아이템 순서는 0이하일 수 없습니다."),
    INVALID_SCHEDULE(HttpStatus.BAD_REQUEST, "SCHEDULEITEM_002", "유효하지 않는 스케줄입니다."),
    SCHEDULEITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULEITEM_003", "해당 스케줄 아이템을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}