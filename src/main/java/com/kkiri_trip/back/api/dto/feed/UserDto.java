package com.kkiri_trip.back.api.dto.feed;

import com.kkiri_trip.back.domain.jpa.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String nickname;

    private String profileUrl;

    private UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .nickname(user.getUserProfile().getNickname())
                .profileUrl(user.getUserProfile().getProfileUrl() != null ? user.getUserProfile().getProfileUrl() : null)
                .build();
    }
}
