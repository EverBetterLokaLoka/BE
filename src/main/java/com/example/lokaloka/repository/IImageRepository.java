package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
    void deleteByUserId(Long userId);
    Image findByUserIdAndType(Long userId, String type);
    @Modifying
    @Query("DELETE FROM Image i WHERE i.user.id = :userId AND i.type = :type")
    void deleteByUserIdAndType(@Param("userId") Long userId, @Param("type") String type);

}
