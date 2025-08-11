package com.green.greengram.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class FeedLikeIds {
    private Long feedId;
    private Long userId;
}

