package com.kkiri_trip.back.api.dto.user.response;

import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.domain.jpa.user.entity.UserProfile;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreateResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;

    public static UserProfileCreateResponseDto from(User user, UserProfile userProfile){
        return new UserProfileCreateResponseDto(
                user.getId(),
                user.getEmail(),
                userProfile.getNickname(),
                userProfile.getProfileUrl()
        );
    }
}
