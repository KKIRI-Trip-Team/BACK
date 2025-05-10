package com.kkiri_trip.back.domain.user.dto.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateResponseDto {
    private Long Id;
    private String email;
    private String nickname;
    private String profileUrl;
}
