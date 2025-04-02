package com.kkiri_trip.back.global.error.errorcode;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements BaseErrorCode {
    EMPTY_TITLE(HttpStatus.BAD_REQUEST, "FEED_001", "제목은 비어 있을 수 없습니다."),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "FEED_002", "내용은 비어 있을 수 없습니다."),
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "FEED_003", "해당 피드를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}