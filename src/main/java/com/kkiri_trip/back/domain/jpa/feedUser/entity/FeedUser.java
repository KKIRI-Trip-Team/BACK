package com.kkiri_trip.back.domain.jpa.feedUser.entity;

import com.kkiri_trip.back.api.dto.FeedUser.FeedUserDto;
import com.kkiri_trip.back.domain.jpa.common.entity.BaseEntity;
import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedUserStatus status;

    @Column(nullable = false)
    private boolean isHost;

    public FeedUserDto toDto(){
        return new FeedUserDto(this.getId(), feed.getId(), user.getId(), this.status, this.isHost);
    }

    public static List<FeedUserDto> toDtoList(List<FeedUser> feedUsers)
    {
        return feedUsers.stream()
                .map(FeedUser::toDto)
                .collect(Collectors.toList());
    }

    public boolean isApproved() {
        return status == FeedUserStatus.APPROVED;
    }

    public boolean isRejected() {
        return status == FeedUserStatus.REJECTED;
    }

    public void approve() {
        status = FeedUserStatus.APPROVED;
    }

    public void reject() {
        status = FeedUserStatus.REJECTED;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }
}
