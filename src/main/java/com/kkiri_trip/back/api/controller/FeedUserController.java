package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.FeedUser.FeedUserDto;
import com.kkiri_trip.back.domain.jpa.feedUser.service.FeedUserService;
import com.kkiri_trip.back.domain.jpa.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed-users")
public class FeedUserController {

    private final FeedUserService feedUserService;

    // 피드 참여 요청
    @PostMapping("/{feedId}/join")
    public ResponseEntity<ApiResponseDto<Void>> joinFeed(@PathVariable Long feedId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        feedUserService.joinRequestFeed(feedId, userId);
        return ApiResponseDto.from(HttpStatus.OK, "피드 참여 요청 완료", null);
    }

    @GetMapping("/{feedId}/approved")
    public ResponseEntity<ApiResponseDto<List<FeedUserDto>>> getApprovedUsers(@PathVariable Long feedId) {
        List<FeedUserDto> approvedUsers = feedUserService.getApprovedUsers(feedId);
        return ApiResponseDto.from(HttpStatus.OK, "피드에 참여한 유저 리스트 조회", approvedUsers);
    }

    // 피드 참여 승인
    @PostMapping("/{feedUserId}/approve")
    public ResponseEntity<ApiResponseDto<Void>> approveJoin(@PathVariable Long feedUserId) {
        feedUserService.approveJoin(feedUserId);
        return ApiResponseDto.from(HttpStatus.OK, "피드 참여 승인 완료", null);
    }

    // 피드 참여 거절
    @PostMapping("/{feedUserId}/reject")
    public ResponseEntity<ApiResponseDto<Void>> rejectJoin(@PathVariable Long feedUserId) {
        feedUserService.rejectJoin(feedUserId);
        return ApiResponseDto.from(HttpStatus.OK, "피드 참여 거절 완료", null);
    }

    // 특정 피드에 참여 신청한 유저 리스트 조회
    @GetMapping("/{feedId}/pending")
    public ResponseEntity<ApiResponseDto<List<FeedUserDto>>> getPendingUsers(@PathVariable Long feedId) {
        List<FeedUserDto> pendingUsers = feedUserService.getPendingUsers(feedId);
        return ApiResponseDto.from(HttpStatus.OK, "참여 요청 대기중인 유저 리스트 조회", pendingUsers);
    }


}
