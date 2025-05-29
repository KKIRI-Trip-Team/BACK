package com.kkiri_trip.back.domain.jpa.image.controller;

import com.kkiri_trip.back.domain.jpa.image.dto.Request.ImageRequestDto;
import com.kkiri_trip.back.domain.jpa.image.dto.Response.ImageResponseDto;
import com.kkiri_trip.back.domain.jpa.image.dto.Response.ImageUrlResponse;
import com.kkiri_trip.back.domain.jpa.image.service.ImageService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponseDto<ImageResponseDto>> getUploadPresignedUrl(@RequestBody ImageRequestDto imageRequestDto){
        ImageResponseDto imageResponseDto = imageService.generateUploadPresignedUrl(imageRequestDto);
        return ApiResponseDto.from(HttpStatus.OK, "Presigned URL 발급 성공", imageResponseDto);
    }

    @GetMapping("/url")
    public ResponseEntity<ApiResponseDto<ImageUrlResponse>> getImageUrl(@RequestParam(name = "key") String key){
        ImageUrlResponse imageUrlResponse = imageService.getImageUrl(key);
        return ApiResponseDto.from(HttpStatus.OK,"이미지 URL 조회 성공", imageUrlResponse);
    }
}
