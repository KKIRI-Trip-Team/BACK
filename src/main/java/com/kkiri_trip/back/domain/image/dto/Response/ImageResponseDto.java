package com.kkiri_trip.back.domain.image.dto.Response;

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
