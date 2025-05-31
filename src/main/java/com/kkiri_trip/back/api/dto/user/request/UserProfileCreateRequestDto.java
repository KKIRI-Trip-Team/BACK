package com.kkiri_trip.back.api.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreateRequestDto {
    private String email;

    @Size(min = 2, max = 16, message = "닉네임은 2자 이상 16자 이하여야 합니다.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]+$",
            message = "닉네임은 한글, 영어, 숫자만 사용할 수 있으며 특수문자와 공백은 사용할 수 없습니다."
    )
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    private String profileUrl;
}
