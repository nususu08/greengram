package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FeedPic extends CreatedAt {
    @EmbeddedId
    private FeedPicIds feedPicIds;

    @ManyToOne
    @MapsId("feedId")
    @JoinColumn(name = "feed_id")
    private Feed feed;
}
