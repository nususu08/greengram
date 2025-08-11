package com.green.greengram.application.feed;

import com.green.greengram.application.feed.model.FeedGetDto;
import com.green.greengram.application.feed.model.FeedGetRes;
import com.green.greengram.application.feed.model.FeedPostReq;
import com.green.greengram.application.feed.model.FeedPostRes;
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
    private final FeedMapper feedMapper;
    private final ImgUploadManager imgUploadManager;

    @Transactional
    public FeedPostRes postFeed(long signedUserId, FeedPostReq feedPostReq, List<MultipartFile> pics) {
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
            return new FeedPostRes(feed.getFeedId(), fileNames);
    }

    public List<FeedGetRes> getFeedList(FeedGetDto dto) {
        List<FeedGetRes> list = feedMapper.findAllLimitedTo(dto);
        //각 피드에서 사진 가져오기
        for(FeedGetRes feedGetRes : list) {
            feedGetRes.setPics(feedMapper.findAllPicByFeedId(feedGetRes.getFeedId()));
        }
        return list;
    }
}
