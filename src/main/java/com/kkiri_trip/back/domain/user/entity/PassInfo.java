package com.kkiri_trip.back.domain.user.entity;

import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.global.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passInfo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassInfo extends BaseEntity {
    // TODO : 추후 Pass 연동 후 정보 제공
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mobile_number;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
