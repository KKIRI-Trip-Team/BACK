package com.kkiri_trip.back.domain.dashboard.entity;

import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "dashboard")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard extends BaseEntity {

    @Column(nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToOne(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private UserRank userRank;
}
