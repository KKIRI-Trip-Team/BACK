package com.kkiri_trip.back.domain.feed.repository;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedRepositoryCustom {
    Page<Feed> searchFeeds(String keyword, Pageable pageable);
}
