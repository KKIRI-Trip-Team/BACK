package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.place.PlaceDto;
import com.kkiri_trip.back.domain.mongo.place.service.PlaceService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<PlaceDto>> createPlace(@RequestBody PlaceDto placeDto) {
        return ApiResponseDto.from(HttpStatus.OK, "장소 생성", placeService.savePlace(placeDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<PlaceDto>>> getAllPlaces() {
        return ApiResponseDto.from(HttpStatus.OK, "장소 리스트 조회", placeService.getAllPlaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<PlaceDto>> getPlace(@PathVariable String id) {
        return ApiResponseDto.from(HttpStatus.OK, "장소 조회", placeService.getPlaceById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto<PlaceDto>> updatePlaceById(@PathVariable String id, @RequestBody PlaceDto placeDto) {
        return ApiResponseDto.from(HttpStatus.OK, "장소 수정", placeService.updatePlace(id, placeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deletePlaceById(@PathVariable String id) {
        placeService.deleteFeed(id);
        return ApiResponseDto.from(HttpStatus.OK, "장소 삭제", null);
    }
}
