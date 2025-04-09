package com.kkiri_trip.back.domain.user.dto.Response;

import com.kkiri_trip.back.domain.user.entity.User;
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

    public static UserResponseDto from(User user){
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}
