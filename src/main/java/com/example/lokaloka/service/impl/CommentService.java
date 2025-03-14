package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.CommentReqDTO;
import com.example.lokaloka.domain.dto.resdto.CommentResDTO;
import com.example.lokaloka.domain.entity.Comment;
import com.example.lokaloka.domain.entity.Post;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.ICommentRepository;
import com.example.lokaloka.repository.IPostRepository;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final ICommentRepository commentRepository;
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;

    @Override
    public CommentReqDTO createComment(CommentReqDTO commentReqDTO) {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();  // Lấy email từ principal (người dùng đã đăng nhập)

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy post từ database theo postId
        Post post = postRepository.findById(commentReqDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Tạo mới comment và thiết lập các giá trị
        Comment comment = Comment.builder()
                .content(commentReqDTO.getContent())
                .post(post)  // Liên kết với post
                .user(user)
                .isDestroyed(false)
                .createdAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()))
                .build();

        // Lưu comment vào database
        Comment savedComment = commentRepository.save(comment);

        // Chuyển đổi comment entity thành CommentResDTO để trả về
        return convertToDTO(savedComment);
    }

    @Override
    public CommentReqDTO updateComment(Long commentId, CommentReqDTO commentReqDTO) {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        // Lấy user từ database dựa trên email
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm kiếm comment theo commentId
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Kiểm tra xem người dùng có phải là người đã tạo comment này không
        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("You are not authorized to update this comment");
        }

        // Cập nhật nội dung comment và thời gian cập nhật
        comment.setContent(commentReqDTO.getContent());
        comment.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

        // Lưu lại comment đã cập nhật vào database
        Comment updatedComment = commentRepository.save(comment);

        // Chuyển đổi comment entity thành CommentReqDTO để trả về
        return convertToDTO(updatedComment);
    }

    @Override
    public List<CommentReqDTO> getAllComment(Long postId) {
        // Lấy thông tin người dùng hiện tại (tùy chọn nếu bạn cần kiểm tra quyền)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        // Lấy tất cả comment của postId từ database
        List<Comment> comments = commentRepository.findByPostId(postId);  // Phương thức tìm kiếm các comment theo postId

        // Chuyển đổi các comment thành DTO và trả về
        return comments.stream()
                .map(this::convertToDTO)  // Chuyển đổi comment thành CommentReqDTO
                .collect(Collectors.toList());
    }
    @Override
    public void deleteComment(Long commentId) {

    }

    private CommentReqDTO convertToDTO(Comment comment) {
        return CommentReqDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())  // Lấy postId từ post entity
                .userId(comment.getUser().getId())
                .isDestroyed(comment.isDestroyed())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
