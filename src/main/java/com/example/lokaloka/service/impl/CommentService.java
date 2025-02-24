// CommentService.java
package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.CommentReqDTO;
import com.example.lokaloka.domain.dto.reqdto.WebSocketEventDTO;
import com.example.lokaloka.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public CommentReqDTO createComment(CommentReqDTO commentReqDTO) {
        // Implement your comment creation logic here
        CommentReqDTO createdComment = new CommentReqDTO();
        createdComment.setCreatedAt( Timestamp.from(Instant.now()));
        createdComment.setContent(commentReqDTO.getContent());

                messagingTemplate.convertAndSend(
                        "/topic/posts/" + commentReqDTO.getPostId() + "/comments",
                        new WebSocketEventDTO("NEW_COMMENT", createdComment)
                );

        return createdComment;
    }

    @Override
    public void deleteComment(Long commentId) {
        // Get comment details first
        CommentReqDTO comment = new CommentReqDTO();
        comment.setId(commentId);
                // Implement your delete logic here

                messagingTemplate.convertAndSend(
                        "/topic/posts/" + comment.getPostId() + "/comments",
                        new WebSocketEventDTO("DELETE_COMMENT", commentId)
                );
    }
}
