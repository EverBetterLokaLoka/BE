package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.*;
import com.example.lokaloka.domain.entity.*;
import com.example.lokaloka.repository.*;
import com.example.lokaloka.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final IImageRepository imageRepository;

    @Override
    public PostReqDTO createPost(PostReqDTO postReqDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .title(postReqDTO.getTitle())
                .content(postReqDTO.getContent())
                .user(user)
                .is_destroyed(false)
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
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
    @Transactional(readOnly = true)
    public List<PostReqDTO> getUserPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy danh sách posts cơ bản
        List<Post> basePosts = postRepository.findActivePostsByUserId(user.getId());

        if (basePosts.isEmpty()) {
            return Collections.emptyList();
        }

        // Lấy danh sách ID của các post
        List<Long> postIds = basePosts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // Tạo map để lưu trữ post theo ID
        Map<Long, Post> postMap = new HashMap<>();
        for (Post post : basePosts) {
            postMap.put(post.getId(), post);
        }

        // Nạp comments
        List<Post> postsWithComments = postRepository.fetchPostComments(postIds);
        for (Post postWithComments : postsWithComments) {
            Post basePost = postMap.get(postWithComments.getId());
            if (basePost != null) {
                basePost.setComments(postWithComments.getComments());
            }
        }

        // Nạp likes
        List<Post> postsWithLikes = postRepository.fetchPostLikes(postIds);
        for (Post postWithLikes : postsWithLikes) {
            Post basePost = postMap.get(postWithLikes.getId());
            if (basePost != null) {
                basePost.setLikes(postWithLikes.getLikes());
            }
        }

        // Nạp images
        List<Post> postsWithImages = postRepository.fetchPostImages(postIds);
        for (Post postWithImages : postsWithImages) {
            Post basePost = postMap.get(postWithImages.getId());
            if (basePost != null) {
                basePost.setImages(postWithImages.getImages());
            }
        }

        // Chuyển đổi sang DTO
        return postMap.values().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PostReqDTO convertToDTO(Post post) {
        List<Comment> activeComments = post.getComments() != null ?
                post.getComments().stream()
                        .filter(comment -> !comment.isDestroyed())
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        List<Like> likes = post.getLikes() != null ? post.getLikes() : Collections.emptyList();

        // Chỉ lấy images có type = "post"
        List<ImageReqDTO> imageDTOs = post.getImages() != null ?
                post.getImages().stream()
                        .filter(image -> "post".equals(image.getType()))
                        .map(this::convertToImageDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        // Lấy avatar của User đăng bài nhưng không đưa vào danh sách images
        Image userAvatar = imageRepository.findByUserIdAndType(post.getUser().getId(), "avatar");
        String avatarUrl = userAvatar != null ? userAvatar.getContent() : null;

        // Convert comments to DTOs
        List<CommentReqDTO> commentDTOs = activeComments.stream()
                .map(comment -> {
                    Image commentUserAvatar = imageRepository.findByUserIdAndType(comment.getUser().getId(), "avatar");

                    return CommentReqDTO.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .postId(post.getId())
                            .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                            .userEmail(comment.getUser() != null ? comment.getUser().getEmail() : null)
                            .userName(comment.getUser() != null ? comment.getUser().getFull_name() : null)
                            .avatar(commentUserAvatar != null ? commentUserAvatar.getContent() : null)
                            .isDestroyed(comment.isDestroyed())
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        // Convert likes to DTOs
        List<LikeReqDTO> likeDTOs = likes.stream()
                .map(like -> LikeReqDTO.builder()
                        .id(like.getId())
                        .postId(post.getId())
                        .userId(like.getUser() != null ? like.getUser().getId() : null)
                        .userEmail(like.getUser() != null ? like.getUser().getEmail() : null)
                        .createdAt(like.getCreated_at())
                        .build())
                .collect(Collectors.toList());

        return PostReqDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user_id(post.getUser() != null ? post.getUser().getId() : null)
                .userEmail(post.getUser() != null ? post.getUser().getEmail() : null)
                .userName(post.getUser() != null ? post.getUser().getFull_name() : null)
                .avatar(avatarUrl)
                .isDestroyed(post.is_destroyed())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(commentDTOs)
                .likes(likeDTOs)
                .images(imageDTOs)  // Chỉ chứa ảnh có type = "post"
                .likeCount(likeDTOs.size())
                .commentCount(commentDTOs.size())
                .build();
    }

    private ImageReqDTO convertToImageDTO(Image image) {
        return ImageReqDTO.builder()
                .id(image.getId())
                .content(image.getContent())
                .shares(image.getShares())
                .locationId(image.getLocation() != null ? image.getLocation().getId() : null)
                .userId(image.getUser() != null ? image.getUser().getId() : null)
                .postId(image.getPost() != null ? image.getPost().getId() : null)
                .mapId(image.getMap() != null ? image.getMap().getId() : null)
                .activityId(image.getActivity() != null ? image.getActivity().getId() : null)
                .userEmail(image.getUser() != null ? image.getUser().getEmail() : null)
                .type(image.getType())
                .created_at(image.getCreated_at())
                .updated_at(image.getUpdated_at())
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public List<PostReqDTO> getAllPosts() {
        List<Post> posts = postRepository.findAllByIsDestroyedFalseOrderByCreatedAtDesc();

        if (posts.isEmpty()) {
            return Collections.emptyList();
        }

        return posts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


}