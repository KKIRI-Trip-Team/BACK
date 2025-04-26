package com.kkiri_trip.back.domain.feedUser.entity;

import com.kkiri_trip.back.api.dto.FeedUser.FeedUserDto;
import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FeedUser extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FeedUserDto toDto(){
        return new FeedUserDto(this.getId(), feed.getId(), user.getId());
    }

    public static List<FeedUserDto> toDtoList(List<FeedUser> feedUsers)
    {
        return feedUsers.stream()
                .map(FeedUser::toDto)
                .collect(Collectors.toList());
    }

}
