package com.kkiri_trip.back.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedUserErrorCode implements BaseErrorCode {

    FEEDUSER_NOT_FOUND(HttpStatus.NOT_FOUND, "FEEDUSER_001", "피드 유저를 찾을 수 없습니다."),
    ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "FEEDUSER_002", "이미 승인된 신청입니다."),
    ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "FEEDUSER_003", "이미 거절된 신청입니다."),
    NOT_PENDING(HttpStatus.BAD_REQUEST, "FEEDUSER_004", "대기중인 상태가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}