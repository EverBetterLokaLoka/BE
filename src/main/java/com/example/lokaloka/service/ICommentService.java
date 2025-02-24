package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.CommentReqDTO;

public interface ICommentService {
    CommentReqDTO createComment(CommentReqDTO commentReqDTO);
    void deleteComment(Long commentId);
}
