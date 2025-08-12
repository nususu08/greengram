package com.green.greengram.application.feedcomment;

import com.green.greengram.application.feedcomment.model.FeedCommentPostReq;
import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedComment;
import com.green.greengram.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCommentService {
    private final FeedCommentRepository feedCommentRepository;

    public long postFeedComment(long signedUserId, FeedCommentPostReq req) {
        Feed feedId = Feed.builder()
                .feedId(req.getFeedId())
                .build();

        User userId = new User();
        userId.setUserId(signedUserId);

        FeedComment feedComment = FeedComment.builder()
                .user(userId)
                .feed(feedId)
                .comment(req.getComment())
                .build();
        FeedComment id = feedCommentRepository.save(feedComment);

        return id.getFeedCommentId();
    }
}
