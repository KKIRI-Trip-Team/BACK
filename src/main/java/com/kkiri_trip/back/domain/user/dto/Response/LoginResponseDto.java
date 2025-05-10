package com.kkiri_trip.back.domain.user.dto.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String email;
    private String nickname;
    private String profileUrl;


}
