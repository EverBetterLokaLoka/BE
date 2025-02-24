package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.LikeReqDTO;

public interface ILikeService {
    LikeReqDTO toggleLike(Long postId, Long userId);
}
