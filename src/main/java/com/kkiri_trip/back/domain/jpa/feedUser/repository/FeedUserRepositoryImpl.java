package com.kkiri_trip.back.domain.jpa.feedUser.repository;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.entity.QFeed;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUserStatus;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.QFeedUser;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public User findHostByFeedId(Long feedId) {
        QFeedUser qFeedUser = QFeedUser.feedUser;

        return queryFactory
                .select(qFeedUser.user)
                .from(qFeedUser)
                .where(
                        qFeedUser.feed.id.eq(feedId)
                                .and(qFeedUser.isHost.isTrue())  // isHost == true 조건
                )
                .fetchOne();  // 유일한 1명 반환
    }


    @Override
    public Optional<FeedUser> findHostFeedUserByFeedId(Long feedId) {
        QFeedUser feedUser = QFeedUser.feedUser;

        return Optional.ofNullable(queryFactory
                .selectFrom(feedUser)
                .where(feedUser.feed.id.eq(feedId)
                        .and(feedUser.isHost.eq(true)))
                .fetchOne());
    }

    @Override
    public Optional<FeedUser> findByFeedIdAndUserId(Long feedId, Long userId) {
        QFeedUser feedUser = QFeedUser.feedUser;

        return Optional.ofNullable(queryFactory
                .selectFrom(feedUser)
                .where(feedUser.feed.id.eq(feedId)
                        .and(feedUser.user.id.eq(userId)))
                .fetchOne());
    }
}
