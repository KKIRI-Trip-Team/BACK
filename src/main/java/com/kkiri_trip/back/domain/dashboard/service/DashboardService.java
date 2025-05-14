package com.kkiri_trip.back.domain.dashboard.service;

import com.kkiri_trip.back.domain.dashboard.dto.DashboardDto;
import com.kkiri_trip.back.domain.dashboard.entity.Dashboard;
import com.kkiri_trip.back.domain.dashboard.repository.DashboardRepository;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.global.common.dto.PageResponseDto;
import com.kkiri_trip.back.global.error.errorcode.DashboardErrorCode;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.DashboardException;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;

    public Dashboard createDashboard(User user){
       if( dashboardRepository.findByUser(user).isPresent()){
                throw new DashboardException(DashboardErrorCode.ALREADY_EXISTS);
        }

        Dashboard dashboard = Dashboard.builder()
                .user(user)
                .viewCount(0L)
                .build();

        return dashboardRepository.save(dashboard);
    }


    public DashboardDto getMyDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Dashboard dashboard = dashboardRepository.findByUser(user)
                .orElseThrow(() -> new DashboardException(DashboardErrorCode.NOT_FOUND));

        return DashboardDto.builder()
                .id(dashboard.getId())
                .email(user.getEmail())
                .nickname(user.getUserProfile().getNickname())
                .profileUrl(user.getUserProfile().getProfileUrl())
                .viewCount(dashboard.getViewCount())
                .build();
    }

    public PageResponseDto<DashboardDto> searchDashboardsByNickname(String nickname, Pageable pageable){
        Page<Dashboard> dashboardPage = dashboardRepository.searchDashboards(nickname, pageable);
        Page<DashboardDto> dashboardDtos = dashboardPage.map(DashboardDto::from);
        return new PageResponseDto<>(dashboardDtos);
    }
}
