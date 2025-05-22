package com.kkiri_trip.back.domain.jpa.user.dto.Response;

import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.domain.jpa.user.entity.UserProfile;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;

    public static UserResponseDto from(User user, UserProfile userProfile){
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                userProfile.getNickname(),
                userProfile.getProfileUrl()
        );
    }
}
