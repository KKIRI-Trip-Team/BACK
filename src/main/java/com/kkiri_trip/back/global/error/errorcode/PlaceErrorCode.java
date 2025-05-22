package com.kkiri_trip.back.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlaceErrorCode implements BaseErrorCode {
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE_001", "장소를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}