package com.kkiri_trip.back.domain.feedUser.repository;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.user.entity.User;

import java.util.List;

public interface FeedUserCustomRepository {

    List<Feed> findFeedsByUser(User user);
    List<User> findUsersByFeed(Feed feed);
    List<FeedUser> findByFeedIdAndStatusApproved(Long feedId);
}
