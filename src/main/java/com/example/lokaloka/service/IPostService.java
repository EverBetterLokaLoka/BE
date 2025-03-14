package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.PostReqDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPostService {
    PostReqDTO createPost(PostReqDTO postReqDTO);
    PostReqDTO updatePost(Long id, PostReqDTO postReqDTO);
    ResponseEntity<?> deletePost(Long id);
    List<PostReqDTO> getUserPosts();
    List<PostReqDTO> getAllPosts();

}
