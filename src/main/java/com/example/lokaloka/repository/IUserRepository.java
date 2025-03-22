package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Follower;
import com.example.lokaloka.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByFullName(@Param("keyword") String keyword);


}