package com.kkiri_trip.back.domain.user.entity;

import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userProfile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity {
    @Column(nullable = false)
    private String nickname;
    @Column(name = "profile_url", nullable = false, length = 2000)
    private String profileUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void createProfile(String nickname, String profileUrl) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

}
