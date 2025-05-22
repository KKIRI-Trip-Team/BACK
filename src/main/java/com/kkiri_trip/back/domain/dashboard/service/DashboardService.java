package com.kkiri_trip.back.domain.dashboard.service;

import com.kkiri_trip.back.domain.dashboard.dto.DashboardDto;
import com.kkiri_trip.back.domain.dashboard.entity.Dashboard;
import com.kkiri_trip.back.domain.dashboard.entity.UserRank;
import com.kkiri_trip.back.domain.dashboard.repository.DashboardRepository;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.global.common.dto.PageResponseDto;
import com.kkiri_trip.back.global.enums.TierLevel;
import com.kkiri_trip.back.global.error.errorcode.DashboardErrorCode;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.DashboardException;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;

    public void createDashboard(User user){
       if(dashboardRepository.existsByUser(user)){
                throw new DashboardException(DashboardErrorCode.ALREADY_EXISTS);
        }

        Dashboard dashboard = Dashboard.builder()
                .user(user)
                .viewCount(0L)
                .build();

        TierLevel tierLevel = TierLevel.fromScore(0L);

        UserRank userRank = UserRank.builder()
                .dashboard(dashboard)
                .tier(tierLevel)
                .score(0L)
                .build();

        dashboard.setUserRank(userRank);

        dashboardRepository.save(dashboard);
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
                .tierName(dashboard.getUserRank().getTier().getName())
                .build();
    }

    public PageResponseDto<DashboardDto> searchDashboardsByNickname(String nickname, Pageable pageable){
        Page<Dashboard> dashboardPage = dashboardRepository.searchDashboards(nickname, pageable);
        Page<DashboardDto> dashboardDtos = dashboardPage.map(DashboardDto::from);
        return new PageResponseDto<>(dashboardDtos);
    }

    @Transactional
    public void updateScoreAndTier(Long userId, long scoreToAdd){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Dashboard dashboard = dashboardRepository.findByUser(user)
                .orElseThrow(() -> new DashboardException(DashboardErrorCode.NOT_FOUND));

        UserRank userRank = dashboard.getUserRank();

        long newScore = userRank.getScore() + scoreToAdd;
        userRank.setScore(newScore);

        TierLevel newTier = TierLevel.fromScore(newScore);
        userRank.setTier(newTier);

    }
}

