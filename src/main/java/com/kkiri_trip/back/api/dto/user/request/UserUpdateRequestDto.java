package com.kkiri_trip.back.api.dto.user.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String email;
    private String nickname;
    private String profileUrl;
    private String password;
    private String confirmPassword;

}
