package com.kkiri_trip.back.domain.dashboard.entity;

import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.global.enums.TierLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "rank")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRank extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TierLevel tier;

    private Long score;
}
