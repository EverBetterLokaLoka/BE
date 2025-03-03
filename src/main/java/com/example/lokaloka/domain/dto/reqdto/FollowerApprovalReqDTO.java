package com.example.lokaloka.domain.dto.reqdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowerApprovalReqDTO {
    private Long followerId;
    private Long followedId;
}
