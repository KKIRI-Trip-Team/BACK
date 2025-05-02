package com.kkiri_trip.back.domain.user.entity;

import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.global.enums.Gender;
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

    private String nickname;

    private String profileUrl;

    // TODO : 추후 Pass 연동 후 정보 제공
//    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
    private String mobile_number;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // private Long temperature
    // private Long review

    public void createProfile(String nickname, String profileUrl) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

}
