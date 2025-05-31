package com.kkiri_trip.back.global.error.errorcode;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements BaseErrorCode {

    EMPTY_TITLE(HttpStatus.BAD_REQUEST, "FEED_001", "제목은 비어 있을 수 없습니다."),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "FEED_002", "내용은 비어 있을 수 없습니다."),
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "FEED_003", "해당 피드를 찾을 수 없습니다."),

    INVALID_REGION(HttpStatus.BAD_REQUEST, "FEED_004", "유효하지 않은 지역입니다."),
    INVALID_PERIOD(HttpStatus.BAD_REQUEST, "FEED_005", "유효하지 않은 기간입니다."),
    INVALID_GENDER(HttpStatus.BAD_REQUEST, "FEED_006", "유효하지 않은 성별입니다."),
    INVALID_AGE_GROUP(HttpStatus.BAD_REQUEST, "FEED_007", "유효하지 않은 연령대입니다."),
    INVALID_COST(HttpStatus.BAD_REQUEST, "FEED_008", "유효하지 않은 비용입니다."),
    INVALID_TRIP_STYLE(HttpStatus.BAD_REQUEST, "FEED_008","유효하지 않은 여행 " +
            "스타일입니다." );

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
