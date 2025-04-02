package com.kkiri_trip.back.api.dto.Feed;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedDto {
    private Long id;

//    @NotBlank(message = "제목은 비어 있을 수 없습니다.")
    private String title;

//    @NotBlank(message = "내용은 비어 있을 수 없습니다.")
    private String content;
}
