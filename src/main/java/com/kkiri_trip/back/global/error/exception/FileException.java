package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.FileErrorCode;
import lombok.Getter;

@Getter
public class FileException extends RuntimeException {
    private final FileErrorCode errorCode;

    public FileException(FileErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}