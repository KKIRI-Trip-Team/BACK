package com.kkiri_trip.back.api.dto.image.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDto {
    private String fileName;
    private String contentType;
    private Long contentLength;
}
