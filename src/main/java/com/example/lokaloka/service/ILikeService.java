package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.LikeReqDTO;

import java.util.List;

public interface ILikeService {
    LikeReqDTO toggleLike(Long postId);
    List<LikeReqDTO> getAllLikeForPost(Long postId);
}
