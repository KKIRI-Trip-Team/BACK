package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.service.FeedService;
import com.kkiri_trip.back.domain.feedUser.service.FeedUserService;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.service.UserService;
import com.kkiri_trip.back.domain.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    private final FeedService feedService;

    private final FeedUserService feedUserService;

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ApiResponseDto<List<FeedDto>>> getFeeds()
    {
        return ApiResponseDto.from(HttpStatus.OK, "피드 리스트 조회", feedService.getAllFeeds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FeedDto>> getFeedById(@PathVariable Long id)
    {
        return ApiResponseDto.from(HttpStatus.OK, "피드 조회", feedService.getFeedById(id));
    }

    @GetMapping("/user/{userId}/feeds")
    public ResponseEntity<ApiResponseDto<List<FeedDto>>> getFeedsByUser(@PathVariable Long userId) {
        List<FeedDto> feedDtos = feedUserService.findFeedsByUser(userId);
        return ApiResponseDto.from(HttpStatus.OK, "사용자 피드 조회", feedDtos);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<ApiResponseDto<List<User>>> getUserByFeed(@PathVariable Long id) {
        List<User> user = feedUserService.findUsersByFeed(id);
        return ApiResponseDto.from(HttpStatus.OK, "사용자 피드 조회", user);
    }

    @PostMapping()
    public ResponseEntity<ApiResponseDto<FeedDto>> createFeed(@Valid @RequestBody FeedDto feedDto,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        FeedDto createdFeed = feedService.createFeed(feedDto);
        feedUserService.createFeedUser(createdFeed, userDetails);
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

    @PostMapping("/{id}/join")
    public ResponseEntity<ApiResponseDto<Void>> joinFeed(@PathVariable Long id,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();  // 현재 로그인한 유저의 ID 가져오기
        feedUserService.joinFeed(id, userId);
        return ApiResponseDto.from(HttpStatus.OK, "피드 참여 성공", null);
    }

    @GetMapping("/dummy")
    public ResponseEntity<ApiResponseDto<FeedDto>> getDummyFeedById()
    {
        FeedDto feedDto = new FeedDto(1L,"제목1","내용1");
        return ApiResponseDto.from(HttpStatus.OK, "피드 리스트 조회", feedDto);
    }

    @GetMapping("/dummylist")
    public ResponseEntity<ApiResponseDto<List<FeedDto>>> getDummyFeeds()
    {
        FeedDto feedDto1 = new FeedDto(1L,"제목1","내용1");
        FeedDto feedDto2 = new FeedDto(2L,"제목2","내용2");
        FeedDto feedDto3 = new FeedDto(3L,"제목3","내용3");
        FeedDto feedDto4 = new FeedDto(4L,"제목4","내용4");
        return ApiResponseDto.from(HttpStatus.OK, "피드 리스트 조회", List.of(feedDto1,feedDto2,feedDto3,feedDto4));
    }
}
