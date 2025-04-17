package com.kkiri_trip.back.domain.feedUser.service;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.api.dto.FeedUser.FeedUserDto;
import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.feedUser.repository.FeedUserCustomRepository;
import com.kkiri_trip.back.domain.feedUser.repository.FeedUserRepository;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.domain.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedUserService {

    private final FeedUserRepository feedUserRepository;

    private final FeedUserCustomRepository feedUserCustomRepository;

    private final FeedRepository feedRepository;

    private final UserRepository userRepository;

    public void createFeedUser(FeedDto feedDto, CustomUserDetails customUserDetails)
    {
        Feed feed = feedRepository.findById(feedDto.getId()).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        User user = userRepository.findById(customUserDetails.getUser().getId()).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));

        FeedUser feedUser = FeedUser.builder()
                .feed(feed)
                .user(user)
                .build();

        feedUserRepository.save(feedUser);
    }


    // 사용자로 피드를 찾는 메서드
    public List<FeedDto> findFeedsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
        return Feed.toDtoList(feedUserCustomRepository.findFeedsByUser(user));
    }

    // 피드로 유저를 찾는 메서드
    public List<User> findUsersByFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
        return feedUserCustomRepository.findUsersByFeed(feed);
    }

    public void joinFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        FeedUser feedUser = new FeedUser(feed, user);
        feedUserRepository.save(feedUser);
    }

}
