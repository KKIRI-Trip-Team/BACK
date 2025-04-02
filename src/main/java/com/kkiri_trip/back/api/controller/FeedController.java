package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.feed.service.FeedService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    private final FeedService feedService;

    @GetMapping()
    public ResponseEntity<ApiResponseDto<List<FeedDto>>> getfeeds()
    {
        return ApiResponseDto.from(HttpStatus.OK, "피드 리스트 조회", feedService.getAllFeeds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FeedDto>> getfeedById(@PathVariable Long id)
    {
        return ApiResponseDto.from(HttpStatus.OK, "피드 조회", feedService.getFeedById(id));
    }

    @PostMapping()
    public ResponseEntity<ApiResponseDto<FeedDto>> createFeed(@Valid @RequestBody FeedDto feedDto) {
        FeedDto createdFeed = feedService.createFeed(feedDto);
        return ApiResponseDto.from(HttpStatus.CREATED, "피드 생성", createdFeed);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FeedDto>> createFeed(@PathVariable Long id, @Valid @RequestBody FeedDto feedDto) {
        FeedDto updatedFeed = feedService.updateFeed(id, feedDto);
        return ApiResponseDto.from(HttpStatus.OK, "피드 수정", updatedFeed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> createFeed(@PathVariable Long id) {
        feedService.deleteFeed(id);
        return ApiResponseDto.from(HttpStatus.OK, "피드 삭제", null);
    }

}
