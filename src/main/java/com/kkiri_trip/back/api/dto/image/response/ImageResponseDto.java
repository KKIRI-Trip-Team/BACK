package com.kkiri_trip.back.api.dto.image.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDto {
    private String presignedUrl;
    private String key;
}
