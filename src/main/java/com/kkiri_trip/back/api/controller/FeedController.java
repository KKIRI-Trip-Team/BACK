package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.feed.FeedDto;
import com.kkiri_trip.back.api.dto.feed.FeedFilterDto;
import com.kkiri_trip.back.api.dto.feed.attribute.AgeGroup;
import com.kkiri_trip.back.api.dto.feed.attribute.Gender;
import com.kkiri_trip.back.api.dto.feed.attribute.Period;
import com.kkiri_trip.back.api.dto.feed.attribute.Region;
import com.kkiri_trip.back.domain.jpa.feed.entity.TripStyleType;
import com.kkiri_trip.back.domain.jpa.feed.service.FeedService;
import com.kkiri_trip.back.domain.jpa.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import com.kkiri_trip.back.global.common.dto.PageResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<FeedDto>>> getFeeds() {
        return ApiResponseDto.from(HttpStatus.OK, "피드 리스트 조회", feedService.getAllFeeds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FeedDto>> getFeedById(@PathVariable Long id) {
        return ApiResponseDto.from(HttpStatus.OK, "피드 조회", feedService.getFeedById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<FeedDto>> createFeed(@Valid @RequestBody FeedDto feedDto,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        FeedDto createdFeed = feedService.createFeed(feedDto, customUserDetails.getUser().getId());
        return ApiResponseDto.from(HttpStatus.CREATED, "피드 생성", createdFeed);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FeedDto>> updateFeedById(@PathVariable Long id, @Valid @RequestBody FeedDto feedDto) {
        FeedDto updatedFeed = feedService.updateFeed(id, feedDto);
        return ApiResponseDto.from(HttpStatus.OK, "피드 수정", updatedFeed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteFeedById(@PathVariable Long id) {
        feedService.deleteFeed(id);
        return ApiResponseDto.from(HttpStatus.OK, "피드 삭제", null);
    }

    // TODO : 게시글에 대한 정확한 데이터 나오면 DTO 생성 후 응답 값 수정
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponseDto<FeedDto>>> searchFeeds(
            @RequestParam(required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        PageResponseDto<FeedDto> feed = feedService.getFeeds(keyword, pageable);
        return ApiResponseDto.from(HttpStatus.OK, "검색된 키워드로 조회되었습니다.", feed);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponseDto<PageResponseDto<FeedDto>>> filterFeeds(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String tripStyle,
            @RequestParam(required = false) String cost,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        FeedFilterDto feedFilterDto = new FeedFilterDto(region, period, gender, ageGroup, tripStyle, cost);
        PageResponseDto<FeedDto> responseDto = feedService.getFilterFeeds(feedFilterDto, pageable);
        return ApiResponseDto.from(HttpStatus.OK, "필터 검색 성공", responseDto);
    }


    @GetMapping("/dummy")
    public ResponseEntity<ApiResponseDto<FeedDto>> getDummyFeedById() {
        FeedDto dummyFeed = createDummyFeed(1L);
        return ApiResponseDto.from(HttpStatus.OK, "더미 피드 조회", dummyFeed);
    }

    @GetMapping("/dummylist")
    public ResponseEntity<ApiResponseDto<List<FeedDto>>> getDummyFeeds() {
        List<FeedDto> dummyFeeds = List.of(
                createDummyFeed(1L),
                createDummyFeed(2L),
                createDummyFeed(3L),
                createDummyFeed(4L)
        );
        return ApiResponseDto.from(HttpStatus.OK, "더미 피드 리스트 조회", dummyFeeds);
    }

    private FeedDto createDummyFeed(Long id) {
        return FeedDto.builder()
                .id(id)
                .title("제목" + id)
                .content("내용" + id)
                .region(Region.SEOUL)
                .period(Period.DAY_TRIP)
                .gender(Gender.FEMALE)
                .ageGroup(AgeGroup.TWENTIES)
                .cost(50000L * id)
                .tripStyles(List.of(TripStyleType.EXPERIENCE, TripStyleType.KNOWLEDGE))
                .build();
    }

}
