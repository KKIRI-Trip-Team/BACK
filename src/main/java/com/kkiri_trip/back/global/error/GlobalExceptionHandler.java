package com.kkiri_trip.back.global.error;

import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import com.kkiri_trip.back.global.error.exception.FeedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleFeedException(FeedException ex) {
        //  발생 시, 404 Not Found 상태와 함께 ApiResponseDto 형태로 응답
        return ApiResponseDto.from(ex.getErrorCode().getHttpStatus(), ex.getMessage(), null);
    }
}
