package com.green.greengram.application.Feed;

import com.green.greengram.application.Feed.model.FeedPostReq;
import com.green.greengram.config.util.ImgUploadManager;
import com.green.greengram.entity.Feed;
import com.green.greengram.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final ImgUploadManager imgUploadManager;

    @Transactional
    public void postFeed(long signedUserId, FeedPostReq feedPostReq, List<MultipartFile> pics) {
        User writerUser = new User();
        writerUser.setUserId(signedUserId);

        Feed feed = Feed.builder()
                .user(writerUser)
                .contents(feedPostReq.getContents())
                .location(feedPostReq.getLocation())
                .build();

        feedRepository.save(feed); // feed 객체는 영속성을 갖는다.

            List<String> fileNames = imgUploadManager.saveFeedPic(feed.getFeedId(), pics);
            feed.addFeedPics(fileNames);
    }
}
