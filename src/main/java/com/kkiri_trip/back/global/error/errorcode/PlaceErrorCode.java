package com.kkiri_trip.back.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlaceErrorCode implements BaseErrorCode {

    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE_001", "장소를 찾을 수 없습니다."),
    INVALID_PLACE_REQUEST(HttpStatus.BAD_REQUEST, "PLACE_002", "유효하지 않은 장소 요청입니다."),
    DUPLICATE_PLACE(HttpStatus.CONFLICT, "PLACE_003", "이미 등록된 장소입니다."),
    MISSING_LOCATION(HttpStatus.BAD_REQUEST, "PLACE_004", "위치 정보가 누락되었습니다."),
    UNSUPPORTED_COORDINATES(HttpStatus.BAD_REQUEST, "PLACE_005", "지원하지 않는 좌표 형식입니다."),
    BLANK_PLACE_NAME(HttpStatus.BAD_REQUEST, "PLACE_006", "placeName은 null 또는 공백일 수 없습니다."),
    BLANK_ADDRESS_NAME(HttpStatus.BAD_REQUEST, "PLACE_007", "addressName은 null 또는 공백일 수 없습니다."),
    INVALID_COORDINATES(HttpStatus.BAD_REQUEST, "PLACE_008", "위도 또는 경도는 0일 수 없습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
