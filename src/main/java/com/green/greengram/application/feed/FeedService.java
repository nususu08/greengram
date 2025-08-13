package com.green.greengram.application.feed;

import com.green.greengram.application.feed.model.*;
import com.green.greengram.application.feedcomment.FeedCommentMapper;
import com.green.greengram.application.feedcomment.model.FeedCommentGetReq;
import com.green.greengram.application.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.application.feedcomment.model.FeedCommentItem;
import com.green.greengram.config.constants.ConstComment;
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
    private final FeedCommentMapper feedCommentMapper;
    private final FeedMapper feedMapper;
    private final ImgUploadManager imgUploadManager;
    private final ConstComment constComment;

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
        for(FeedGetRes feedGetRes : list) {
            feedGetRes.setPics(feedMapper.findAllPicByFeedId(feedGetRes.getFeedId()));
            //startIdx:0, size: 4
            FeedCommentGetReq req = new FeedCommentGetReq(feedGetRes.getFeedId(), constComment.startIndex, constComment.needForViewCount);
            List<FeedCommentItem> commentList = feedCommentMapper.findAllByFeedIdLimitedTo(req);
            boolean moreComment = commentList.size() > constComment.needForViewCount;
            FeedCommentGetRes feedCommentGetRes = new FeedCommentGetRes(moreComment, commentList);
            feedGetRes.setComments(feedCommentGetRes);
            if(moreComment) { //마지막 댓글 삭제
                commentList.remove(constComment.needForViewCount);
            }
        }
        return list;
    }
}
