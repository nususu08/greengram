package com.green.greengram.application.feedcomment;

import com.green.greengram.application.feedcomment.model.FeedCommentPostReq;
import com.green.greengram.config.model.ResultResponse;
import com.green.greengram.config.model.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/feed/comment")
@RequiredArgsConstructor
public class FeedCommentController {
    private final FeedCommentService feedCommentService;

    @PostMapping
    public ResultResponse<?> postFeedComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @Valid @RequestBody FeedCommentPostReq req) {
        log.info("signedUserId: {}", userPrincipal.getSignedUserId());
        log.info("req: {}", req);
        long feedCommentId = feedCommentService.postFeedComment(userPrincipal.getSignedUserId(), req);
        return new ResultResponse<>("댓글 달기 성공", feedCommentId);
    }
}
