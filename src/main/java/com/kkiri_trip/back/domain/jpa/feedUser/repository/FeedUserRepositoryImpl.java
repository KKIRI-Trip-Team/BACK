package com.kkiri_trip.back.domain.jpa.feedUser.repository;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUserStatus;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.QFeedUser;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kkiri_trip.back.domain.jpa.feedUser.entity.QFeedUser.feedUser;


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

    @Override
    public List<FeedUser> findByFeedIdAndStatusApproved(Long feedId) {
        QFeedUser qFeedUser = QFeedUser.feedUser;  // QueryDSL에서 생성된 Q 클래스
        return queryFactory
                .selectFrom(qFeedUser)
                .where(qFeedUser.feed.id.eq(feedId) // 피드 ID가 일치
                        .and(qFeedUser.status.eq(FeedUserStatus.APPROVED))) // status가 APPROVED인 유저들만
                .fetch();
    }
}
