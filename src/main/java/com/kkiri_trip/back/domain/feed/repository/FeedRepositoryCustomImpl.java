package com.kkiri_trip.back.domain.feed.repository;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.entity.QFeed;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
}
