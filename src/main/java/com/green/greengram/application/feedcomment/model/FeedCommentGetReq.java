package com.green.greengram.application.feedcomment.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@ToString
public class FeedCommentGetReq {
    @Positive
    @NotNull(message = "feed_id값은 필수입니다.")
    private Long feedId;

    @PositiveOrZero
    @NotNull(message = "start_idx값은 필수입니다.")
    private Integer startIdx;

    @Min(20) @Max(50)
    @NotNull(message = "size값은 필수 입니다.")
    private Integer size;

    // private Integer sizePlusOne;

    public FeedCommentGetReq(@BindParam("feed_id")Long feedId
                           , @BindParam("start_idx")Integer startIdx, Integer size) {
        this.feedId = feedId;
        this.startIdx = startIdx;
        this.size = size;
        // this.sizePlusOne = size +1;
    }

    public Integer getSizePlusOne() {
        return size + 1;
    }
}
