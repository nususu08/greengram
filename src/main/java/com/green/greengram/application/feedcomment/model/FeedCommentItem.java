package com.green.greengram.application.feedcomment.model;

import lombok.Getter;

@Getter
public class FeedCommentItem {
    private long feedCommentId;
    private String comment;
    private long writerUserId;
    private String writerUdi;
    private String writerNickName;
    private String writerPic;
}
