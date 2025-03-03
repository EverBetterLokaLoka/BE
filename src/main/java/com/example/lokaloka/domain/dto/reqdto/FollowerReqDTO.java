package com.example.lokaloka.domain.dto.reqdto;

import lombok.Data;

@Data
public class FollowerReqDTO {
    private Long followerId;
    private Long followedId;
    private Long relationshipType;
}
