package com.kkiri_trip.back.domain.dashboard.controller;


import com.kkiri_trip.back.domain.dashboard.dto.DashboardDto;
import com.kkiri_trip.back.domain.dashboard.service.DashboardService;
import com.kkiri_trip.back.domain.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import com.kkiri_trip.back.global.common.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponseDto<DashboardDto>>> searchDashboardsByNickname(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String nickname = customUserDetails.getUser().getUserProfile().getNickname();
        Pageable pageable = PageRequest.of(page - 1, size);
        PageResponseDto<DashboardDto> dashboardDto = dashboardService.searchDashboardsByNickname(nickname, pageable);
        return ApiResponseDto.from(HttpStatus.OK, "검색한 사용자의 대시보드를 조회했습니다.", dashboardDto);
    }

}
