package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.PostReqDTO;
import com.example.lokaloka.domain.entity.Post;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.IPostRepository;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;  // Assuming you have a User repository to fetch the User entity

    @Override
    public PostReqDTO createPost(PostReqDTO postReqDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName(); // Assuming the email is stored as the principal

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .title(postReqDTO.getTitle())
                .content(postReqDTO.getContent())
                .user(user)  // Set the User entity
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

        return convertToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.set_destroyed(true);
        postRepository.save(post);
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
                .user_id(post.getUser().getId())  // Get the user_id from the User entity
                .isDestroyed(post.is_destroyed())
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }
}

