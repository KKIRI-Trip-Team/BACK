package com.kkiri_trip.back.domain.user.dto.Request;

import com.kkiri_trip.back.global.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;
    private String name;
    private String nickname;
    private String mobile_number;
    private Gender gender;
}
