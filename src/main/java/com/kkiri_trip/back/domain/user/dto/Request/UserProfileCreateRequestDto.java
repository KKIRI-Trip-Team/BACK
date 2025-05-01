package com.kkiri_trip.back.domain.user.dto.Request;

import com.kkiri_trip.back.global.enums.Gender;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreateRequestDto {
    private String email;
    private String nickname;
    private String profileUrl;
}
