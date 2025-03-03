package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.CommentReqDTO;

import java.util.List;

public interface ICommentService {
    CommentReqDTO createComment(CommentReqDTO commentReqDTO);
    CommentReqDTO updateComment(Long id,CommentReqDTO commentReqDTO);
    List<CommentReqDTO> getAllComment(Long id);
    void deleteComment(Long commentId);
}
