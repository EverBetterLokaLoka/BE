package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IPostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.is_destroyed = false")
    List<Post> findActivePostsByUserId(@Param("userId") Long userId);

}
