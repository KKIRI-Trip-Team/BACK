package com.kkiri_trip.back.domain.feedUser.repository;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kkiri_trip.back.domain.feedUser.entity.QFeedUser.feedUser;

@Repository
@RequiredArgsConstructor
public class FeedUserRepositoryImpl implements FeedUserCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Feed> findFeedsByUser(User user) {
        return queryFactory.select(feedUser.feed)
                .from(feedUser)
                .where(feedUser.user.eq(user))
                .fetch();
    }

    @Override
    public List<User> findUsersByFeed(Feed feed) {
        return queryFactory.select(feedUser.user)
                .from(feedUser)
                .where(feedUser.feed.eq(feed))
                .fetch();
    }
}
