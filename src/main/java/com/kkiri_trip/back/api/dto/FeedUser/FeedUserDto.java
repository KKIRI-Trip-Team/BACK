package com.kkiri_trip.back.api.dto.FeedUser;

import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedUserDto {
    private Long id;
    private Long feedId;
    private Long userId;
    private FeedUserStatus status;
    private boolean isHost;
}
