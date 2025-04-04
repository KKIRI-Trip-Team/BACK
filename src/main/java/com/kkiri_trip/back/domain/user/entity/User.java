package com.kkiri_trip.back.domain.user.entity;

import com.kkiri_trip.back.global.common.entity.BaseEntity;
import com.kkiri_trip.back.global.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String nickname;

    @Column(nullable = false)
    private String mobile_number;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // private Long temperature
    // private Long review


}
