package com.kkiri_trip.back.domain.feed.repository;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.entity.QFeed;
import com.kkiri_trip.back.domain.feed.entity.QFeedTripStyle;
import com.kkiri_trip.back.domain.feed.entity.QTripStyle;
import com.kkiri_trip.back.domain.feedUser.entity.QFeedUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.kkiri_trip.back.domain.feed.entity.QTripStyle.tripStyle;

@RequiredArgsConstructor
public class FeedRepositoryCustomImpl implements FeedRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Feed> searchFeeds(String keyword, Pageable pageable) {
        QFeed feed = QFeed.feed;

        List<Feed> content = jpaQueryFactory
                .selectFrom(feed)
                .where(
                       feed.title.containsIgnoreCase(keyword)
                                .or(feed.content.containsIgnoreCase(keyword))
                )
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(feed.count())
                .from(feed)
                .where(feed.title.containsIgnoreCase(keyword)
                        .or(feed.content.containsIgnoreCase(keyword)))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    // TODO : 게시글에 대한 정홗한 데이터 나오면 DTO 생성 후 응답 값 수정
    @Override
    public Page<Feed> findMyFeeds(Long userId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QFeedUser feedUser = QFeedUser.feedUser;

        List<Feed> content = jpaQueryFactory
                .select(feed)
                .from(feedUser)
                .join(feedUser.feed, feed)
                .where(feedUser.user.id.eq(userId)
                        .and(feedUser.isHost.isTrue()))
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(feed.count())
                .from(feedUser)
                .join(feedUser.feed, feed)
                .where(feedUser.user.id.eq(userId)
                        .and(feedUser.isHost.isTrue()))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Feed> findAllWithTripStyles() {
        QFeed feed = QFeed.feed;
        QFeedTripStyle feedTripStyle = QFeedTripStyle.feedTripStyle;

        return jpaQueryFactory.selectFrom(feed)
                .leftJoin(feed.feedTripStyles, feedTripStyle).fetchJoin()
                .leftJoin(feedTripStyle.tripStyle, tripStyle).fetchJoin()  // 추가
                .distinct()
                .fetch();
    }

    public Optional<Feed> findWithTripStylesById(Long id) {
        QFeed feed = QFeed.feed;
        QFeedTripStyle feedTripStyle = QFeedTripStyle.feedTripStyle;
        QTripStyle tripStyle = QTripStyle.tripStyle;

        Feed result = jpaQueryFactory.selectFrom(feed)
                .leftJoin(feed.feedTripStyles, feedTripStyle).fetchJoin()
                .leftJoin(feedTripStyle.tripStyle, tripStyle).fetchJoin()
                .where(feed.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
