package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Follower;
import com.example.lokaloka.domain.entity.RelationshipType;
import com.example.lokaloka.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Follower> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    List<Follower> findByFollowedIdAndRelationshipType(Long followedId, RelationshipType relationshipType);

    // Lấy mối quan hệ cả hai chiều và kiểu mối quan hệ là "Friends"
    List<Follower> findByFollowedIdOrFollowerIdAndRelationshipType(Long followedId, Long followerId, RelationshipType relationshipType);

    List<Follower> findByFollowerIdAndRelationshipType(Long followerId, RelationshipType relationshipType);

    List<Follower> findByFollowerAndFollowedAndRelationshipType(User follower, User followed, RelationshipType relationshipType);

}
