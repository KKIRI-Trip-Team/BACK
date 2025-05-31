package com.kkiri_trip.back.api.dto.user.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {
    private Long id;
    private String email;
}
