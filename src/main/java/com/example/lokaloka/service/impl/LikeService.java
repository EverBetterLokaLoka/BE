// LikeService.java
package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.LikeReqDTO;
import com.example.lokaloka.domain.dto.reqdto.WebSocketEventDTO;
import com.example.lokaloka.service.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LikeService implements ILikeService {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public LikeReqDTO toggleLike(Long postId, Long userId) {
        // Implement your like toggle logic here
        LikeReqDTO likeStatus = new LikeReqDTO();
        likeStatus.setPostId(postId);
        likeStatus.setUserId(userId);
        likeStatus.setCreatedAt(Timestamp.from(Instant.now()));

                messagingTemplate.convertAndSend(
                        "/topic/posts/" + postId + "/likes",
                        new WebSocketEventDTO("TOGGLE_LIKE", likeStatus)
                );

        return likeStatus;
    }
}
