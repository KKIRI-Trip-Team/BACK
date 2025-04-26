package com.kkiri_trip.back.api.dto.FeedUser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedUserDto {
    private Long id;
    private Long feedId;
    private Long userId;
}
