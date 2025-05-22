package com.kkiri_trip.back.domain.jpa.dashboard.dto;

import com.kkiri_trip.back.domain.jpa.dashboard.entity.Dashboard;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;
    private Long viewCount;

    public static DashboardDto from(Dashboard dashboard) {
        return DashboardDto.builder()
                .id(dashboard.getId())
                .email(dashboard.getUser().getEmail())
                .nickname(dashboard.getUser().getUserProfile().getNickname())
                .profileUrl(dashboard.getUser().getUserProfile().getProfileUrl())
                .viewCount(dashboard.getViewCount())
                .build();
    }
}
