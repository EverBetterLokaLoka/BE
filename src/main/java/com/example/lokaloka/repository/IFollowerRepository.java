package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Follower;
import com.example.lokaloka.domain.entity.RelationshipType;
import com.example.lokaloka.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Follower> findByFollowedIdOrFollowerId(Long id, Long id1);

    List<Follower> findByFollowerAndFollowed(User follower, User followed);

    List<Follower> findByFollowerAndRelationshipType(User follower, RelationshipType relationshipType);

    @Query("SELECT f FROM Follower f WHERE f.follower.id = :followerId AND f.followed.id = :followedId AND f.relationshipType.typeName = 'Pending'")
    Optional<Follower> findPendingRequest(@Param("followerId") Long followerId, @Param("followedId") Long followedId);

}
