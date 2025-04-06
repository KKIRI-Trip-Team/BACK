package com.kkiri_trip.back.global.error;

import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import com.kkiri_trip.back.global.error.exception.ScheduleItemException;
import com.kkiri_trip.back.global.error.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleFeedException(FeedException ex) {
        return ApiResponseDto.from(ex.getErrorCode().getHttpStatus(), ex.getMessage(), null);
    }

    @ExceptionHandler(ScheduleException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleScheduleException(ScheduleException ex) {
        return ApiResponseDto.from(ex.getErrorCode().getHttpStatus(), ex.getMessage(), null);
    }

    @ExceptionHandler(ScheduleItemException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleScheduleItemException(ScheduleItemException ex) {
        return ApiResponseDto.from(ex.getErrorCode().getHttpStatus(), ex.getMessage(), null);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleUserException(UserException ex) {
        return ApiResponseDto.from(ex.getErrorCode().getHttpStatus(), ex.getMessage(), null);
    }
}
