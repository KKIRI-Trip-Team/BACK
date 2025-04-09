package com.kkiri_trip.back.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER_001", "이미 가입된 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "USER_002", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_003", "사용자를 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "USER_004", "유효하지 않은 토큰입니다."),
    UNAUTHORIZED_UPDATE(HttpStatus.UNAUTHORIZED, "USER_005", "수정 권한이 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}