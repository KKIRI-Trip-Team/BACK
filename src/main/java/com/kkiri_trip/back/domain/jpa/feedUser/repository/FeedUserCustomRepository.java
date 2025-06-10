package com.kkiri_trip.back.domain.jpa.feedUser.repository;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.entity.QFeed;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.QFeedUser;
import com.kkiri_trip.back.domain.jpa.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FeedUserCustomRepository {

    List<Feed> findFeedsByUser(User user);
    List<User> findUsersByFeed(Feed feed);
    List<FeedUser> findByFeedIdAndStatusApproved(Long feedId);
    User findHostByFeedId(Long feedId);
    Optional<FeedUser> findHostFeedUserByFeedId(Long feedId);
    Optional<FeedUser> findByFeedIdAndUserId(Long feedId, Long userId);
}
