package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.PostReqDTO;
import java.util.List;

public interface IPostService {
    PostReqDTO createPost(PostReqDTO postReqDTO);
    PostReqDTO updatePost(Long id, PostReqDTO postReqDTO);
    void deletePost(Long id);
    List<PostReqDTO> getUserPosts(Long userId);
}
