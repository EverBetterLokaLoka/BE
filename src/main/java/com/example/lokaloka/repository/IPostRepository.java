package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostRepository extends JpaRepository<Post, Long> {
    // Truy vấn lấy tất cả dữ liệu trong một lần
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.user u " +
            "WHERE p.user.id = :userId AND p.is_destroyed = false")
    List<Post> findActivePostsByUserId(@Param("userId") Long userId);

    // Truy vấn riêng cho comments
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.comments c " +
            "LEFT JOIN FETCH c.user " +
            "WHERE p.id IN :postIds")
    List<Post> fetchPostComments(@Param("postIds") List<Long> postIds);

    // Truy vấn riêng cho likes
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.likes l " +
            "LEFT JOIN FETCH l.user " +
            "WHERE p.id IN :postIds")
    List<Post> fetchPostLikes(@Param("postIds") List<Long> postIds);

    // Truy vấn riêng cho images
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.images i " +
            "WHERE p.id IN :postIds AND (i.type = 'post' OR i IS NULL)")
    List<Post> fetchPostImages(@Param("postIds") List<Long> postIds);

    @Query("SELECT p FROM Post p WHERE p.is_destroyed = false ORDER BY p.createdAt DESC")
    List<Post> findAllByIsDestroyedFalseOrderByCreatedAtDesc();



}