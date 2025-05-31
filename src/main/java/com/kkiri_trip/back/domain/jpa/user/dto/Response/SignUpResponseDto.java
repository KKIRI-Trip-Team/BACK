package com.kkiri_trip.back.domain.jpa.user.dto.Response;

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
