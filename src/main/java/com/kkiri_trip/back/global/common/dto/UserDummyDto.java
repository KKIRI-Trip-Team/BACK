package com.kkiri_trip.back.global.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDummyDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String mobile_number;
    private String profileUrl;
    private String gender;
}
