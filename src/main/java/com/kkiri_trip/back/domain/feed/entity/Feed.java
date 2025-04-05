package com.kkiri_trip.back.domain.feed.entity;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Feed extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

//    @Column
//    private Long userId;

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_TITLE);
        }
        this.title = title;
    }

    public void setContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_CONTENT);
        }
        this.content = content;
    }

    // Feed 엔티티를 FeedDto로 변환하는 메서드
    public FeedDto toDto() {
        return new FeedDto(this.getId(), this.title, this.content);
    }

    // Feed 리스트를 FeedDto 리스트로 변환하는 Stream 기반 메서드
    public static List<FeedDto> toDtoList(List<Feed> feeds) {
        return feeds.stream()
                .map(Feed::toDto)
                .collect(Collectors.toList());
    }
}
