package com.kkiri_trip.back.domain.user.entity;

import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.domain.dashboard.entity.Dashboard;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PassInfo passInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Dashboard dashboard;

    // private Long temperature
    // private Long review
}
