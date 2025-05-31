package com.kkiri_trip.back.api.dto.dashboard;

import com.kkiri_trip.back.domain.jpa.dashboard.entity.Dashboard;
import com.kkiri_trip.back.global.enums.TierLevel;
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
    private String tierName;

    public static DashboardDto from(Dashboard dashboard) {
        TierLevel tier = dashboard.getUserRank().getTier();

        return DashboardDto.builder()
                .id(dashboard.getId())
                .email(dashboard.getUser().getEmail())
                .nickname(dashboard.getUser().getUserProfile().getNickname())
                .profileUrl(dashboard.getUser().getUserProfile().getProfileUrl())
                .viewCount(dashboard.getViewCount())
                .tierName(tier.getName())
                .build();
    }
}
