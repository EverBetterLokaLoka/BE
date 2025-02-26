package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.LikeReqDTO;
import com.example.lokaloka.domain.entity.Like;
import com.example.lokaloka.domain.entity.Post;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.ILikeRepository;
import com.example.lokaloka.repository.IPostRepository;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.service.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService implements ILikeService {

    private final ILikeRepository likeRepository;
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;

    @Override
    public LikeReqDTO toggleLike(Long postId) {
        // Kiểm tra xem bài viết có tồn tại không
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName(); // Assuming the email is stored as the principal

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra nếu người dùng đã like bài viết này chưa
        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);

        if (existingLike.isPresent()) {
            // Nếu đã like, xóa like
            likeRepository.delete(existingLike.get());
            return LikeReqDTO.builder()
                    .postId(postId)
                    .userId(user.getId())
                    .createdAt(null)
                    .build();
        } else {
            // Nếu chưa like, tạo mới like
            Like newLike = Like.builder()
                    .post(post)
                    .user(user)
                    .created_at(Timestamp.from(Instant.now()))
                    .build();

            Like savedLike = likeRepository.save(newLike);

            return convertToDTO(savedLike);
        }
    }

    private LikeReqDTO convertToDTO(Like like) {
        return LikeReqDTO.builder()
                .id(like.getId())
                .postId(like.getPost().getId())
                .userId(like.getUser().getId())
                .createdAt(like.getCreated_at())
                .build();
    }
}
