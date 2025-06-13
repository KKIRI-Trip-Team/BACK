package com.kkiri_trip.back.domain.jpa.feed.repository;

import com.kkiri_trip.back.api.dto.feed.FeedFilterDto;
import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeedRepositoryCustom {
    Page<Feed> searchFeeds(String keyword, Pageable pageable);
    Page<Feed> findMyFeeds(Long userId, Pageable pageable);
    List<Feed> findAllWithTripStyles();
    Optional<Feed> findWithTripStylesById(Long id);
    Page<Feed> filterFeeds(FeedFilterDto feedFilterRequestDto, Pageable pageable);
}
