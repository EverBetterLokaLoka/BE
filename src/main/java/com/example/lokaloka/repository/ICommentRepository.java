package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long> {
    // Bạn có thể thêm các truy vấn tuỳ chỉnh nếu cần
    List<Comment> findByPostId(Long postId);
}
