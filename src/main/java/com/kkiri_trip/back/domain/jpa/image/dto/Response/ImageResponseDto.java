package com.kkiri_trip.back.domain.jpa.image.dto.Response;

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
