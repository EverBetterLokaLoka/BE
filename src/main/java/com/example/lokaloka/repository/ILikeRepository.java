package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Like;
import com.example.lokaloka.domain.entity.Post;
import com.example.lokaloka.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILikeRepository extends JpaRepository<Like, Long> {
    // Tìm kiếm Like theo Post và User
    Optional<Like> findByPostAndUser(Post post, User user);
}
