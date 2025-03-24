package com.kkiri_trip.back.global.common.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
public record ApiResponseDto<T> (String message, T data, LocalDateTime timestamp) {

    public static <T> ResponseEntity<ApiResponseDto<T>> from(HttpStatus status, String message, T data) {
        return ResponseEntity
                .status(status)
                .body(new ApiResponseDto<>(
                        message,
                        data,
                        LocalDateTime.now()
                ));
    }
}