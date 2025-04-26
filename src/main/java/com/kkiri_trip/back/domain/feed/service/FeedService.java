package com.kkiri_trip.back.domain.feed.service;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.feedUser.service.FeedUserService;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    private final UserRepository userRepository;

    private final FeedUserService feedUserService;

    public List<FeedDto> getAllFeeds()
    {
        return Feed.toDtoList(feedRepository.findAll());
    }

    public FeedDto getFeedById(Long id)
    {
        return feedRepository.findById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND))
                .toDto();
    }

    public FeedDto createFeed(FeedDto feedDto, Long userId) {
        validateFeedDto(feedDto);

        Feed feed = Feed.builder()
                .title(feedDto.getTitle())
                .content(feedDto.getContent())
                .build();

        Feed savedFeed = feedRepository.save(feed);

        feedUserService.createFeedHost(savedFeed.getId(),userId );
        return savedFeed.toDto();
    }


    @Transactional
    public FeedDto updateFeed(Long id, FeedDto feedDto) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        validateFeedDto(feedDto);

        // Dirty Checking
        feed.setTitle(feedDto.getTitle());
        feed.setContent(feedDto.getContent());

        return new FeedDto(feed.getId(), feed.getTitle(), feed.getContent());
    }

    private void validateFeedDto(FeedDto feedDto) {
        if (feedDto.getTitle() == null || feedDto.getTitle().trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_TITLE);
        }
        if (feedDto.getContent() == null || feedDto.getContent().trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_CONTENT);
        }
    }

    private void validateUser(Long userId)
    {
        userRepository.findById(userId).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void deleteFeed(Long id) {
        feedRepository.findById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        feedRepository.deleteById(id);
    }
}
