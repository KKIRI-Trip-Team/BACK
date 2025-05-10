package com.kkiri_trip.back.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements BaseErrorCode {
    EXCEED_SIZE_LIMIT(HttpStatus.BAD_REQUEST, "FILE_001", "파일 크기 제한을 초과했습니다."),
    NOT_MATCHED_TYPE(HttpStatus.BAD_REQUEST, "FILE_002", "이미지 형식만 업로드 할 수 있습니다."),
    INVALID_KEYWORD(HttpStatus.BAD_REQUEST, "FILE_003", "잘못된 이미지 키입니다.");
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}