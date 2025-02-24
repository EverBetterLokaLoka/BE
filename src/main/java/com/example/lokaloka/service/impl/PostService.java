package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.PostReqDTO;
import com.example.lokaloka.domain.dto.reqdto.WebSocketEventDTO;
import com.example.lokaloka.domain.entity.Post;
import com.example.lokaloka.repository.IPostRepository;
import com.example.lokaloka.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final SimpMessagingTemplate messagingTemplate;
    private final IPostRepository postRepository;

    @Override
    public PostReqDTO createPost(PostReqDTO postReqDTO) {
        Post post = Post.builder()
                .title(postReqDTO.getTitle())
                .content(postReqDTO.getContent())
                .user(postReqDTO.getUser())
                .is_destroyed(false)
                .build();

        Post savedPost = postRepository.save(post);

        return convertToDTO(savedPost);
    }

    @Override
    public PostReqDTO updatePost(Long id, PostReqDTO postReqDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postReqDTO.getTitle());
        post.setContent(postReqDTO.getContent());
        post.set_destroyed(postReqDTO.isDestroyed());

        Post updatedPost = postRepository.save(post);

        PostReqDTO response = convertToDTO(updatedPost);

        messagingTemplate.convertAndSend(
                "/topic/posts/" + id,
                new WebSocketEventDTO("UPDATE_POST", response)
        );

        return response;
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.set_destroyed(true);
        postRepository.save(post);

        messagingTemplate.convertAndSend(
                "/topic/posts/" + id,
                new WebSocketEventDTO("DELETE_POST", id)
        );
    }

    @Override
    public List<PostReqDTO> getUserPosts(Long userId) {
        return postRepository.findActivePostsByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PostReqDTO convertToDTO(Post post) {
        return PostReqDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(post.getUser())
                .isDestroyed(post.is_destroyed())
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }
}