package com.kkiri_trip.back.domain.jpa.feedUser.service;

import com.kkiri_trip.back.api.dto.feed.FeedDto;
import com.kkiri_trip.back.api.dto.feedUser.FeedUserDto;
import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUserStatus;
import com.kkiri_trip.back.domain.jpa.feedUser.repository.FeedUserCustomRepository;
import com.kkiri_trip.back.domain.jpa.feedUser.repository.FeedUserRepository;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.domain.jpa.user.repository.UserRepository;
import com.kkiri_trip.back.domain.jpa.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.FeedUserErrorCode;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.FeedUserException;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<FeedDto> findFeedsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
        return Feed.toDtoList(feedUserCustomRepository.findFeedsByUser(user));
    }

    public List<User> findUsersByFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
        return feedUserCustomRepository.findUsersByFeed(feed);
    }

    public void createFeedHost(Long feedId, Long userId)
    {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() ->
                        new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserException(UserErrorCode.USER_NOT_FOUND));

        FeedUser feedUser = new FeedUser(feed, user, FeedUserStatus.APPROVED, true);
        feedUserRepository.save(feedUser);
    }

    public void joinRequestFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> 
                             new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> 
                             new UserException(UserErrorCode.USER_NOT_FOUND));

        FeedUser feedUser = new FeedUser(feed, user, FeedUserStatus.PENDING,false);
        feedUserRepository.save(feedUser);
    }

    @Transactional
    public void approveJoin(Long feedUserId) {
        FeedUser feedUser = feedUserRepository.findById(feedUserId)
                .orElseThrow(() -> new FeedUserException(FeedUserErrorCode.FEEDUSER_NOT_FOUND));

        if (feedUser.isApproved())
            throw new FeedUserException(FeedUserErrorCode.ALREADY_APPROVED);

        feedUser.approve();
    }

    @Transactional
    public void rejectJoin(Long feedUserId) {
        FeedUser feedUser = feedUserRepository.findById(feedUserId)
                .orElseThrow(() -> new FeedUserException(FeedUserErrorCode.FEEDUSER_NOT_FOUND));

        if (feedUser.isApproved())
            throw new FeedUserException(FeedUserErrorCode.ALREADY_REJECTED);

        feedUser.reject();
    }

    public User findHostsByFeed(Long feedId) {
        return feedUserRepository.findByFeedIdAndIsHostTrue(feedId).getUser();
    }

    public List<FeedUserDto> getPendingUsers(Long feedId) {
        List<FeedUser> pendingUsers = feedUserRepository.findByFeedIdAndStatus(feedId, FeedUserStatus.PENDING);
        return FeedUser.toDtoList(pendingUsers);
    }

    public List<FeedUserDto> getApprovedUsers(Long feedId) {
        List<FeedUser> approvedUsers = feedUserCustomRepository.findByFeedIdAndStatusApproved(feedId);
        return FeedUser.toDtoList(approvedUsers);
    }
}
