package com.kkiri_trip.back.domain.jpa.user.dto.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;
    private String accessToken;

}
