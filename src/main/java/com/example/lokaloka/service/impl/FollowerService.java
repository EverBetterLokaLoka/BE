package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.FollowerApprovalReqDTO;
import com.example.lokaloka.domain.dto.reqdto.FollowerReqDTO;
import com.example.lokaloka.domain.dto.resdto.FollowerResDTO;
import com.example.lokaloka.domain.dto.resdto.FriendResDTO;
import com.example.lokaloka.domain.entity.Follower;
import com.example.lokaloka.domain.entity.RelationshipType;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.IFollowerRepository;
import com.example.lokaloka.repository.IRelationshipTypeRepository;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.service.IFollowerService;
import com.example.lokaloka.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowerService implements IFollowerService {

    @Autowired
    private IFollowerRepository followerRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRelationshipTypeRepository relationshipTypeRepository;

    @Override
    public ApiResponse<FollowerResDTO> createFriendship(FollowerReqDTO followerReqDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }

        String loggedInUserEmail = authentication.getName();

        User follower = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        User followed = userRepository.findById(followerReqDTO.getFollowedId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Followed user not found"));

        if (follower.getId().equals(followed.getId())) {
            return ApiResponse.<FollowerResDTO>builder()
                    .status(400)
                    .success(false)
                    .message("You cannot follow yourself")
                    .build();
        }

        RelationshipType relationshipType = relationshipTypeRepository.findByTypeName("Pending")
                .orElseThrow(() -> new RuntimeException("Relationship type not found"));

        List<Follower> existingFollowers = followerRepository.findByFollowerAndFollowedAndRelationshipType(follower, followed, relationshipType);
        if (!existingFollowers.isEmpty()) {
            return ApiResponse.<FollowerResDTO>builder()
                    .status(400)
                    .success(false)
                    .message("You have already sent a follow request to this user")
                    .build();
        }

        Follower followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setFollowed(followed);
        followerEntity.setRelationshipType(relationshipType);
        followerEntity.setCreated_at(new Timestamp(System.currentTimeMillis()));
        followerEntity.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        Follower savedFollower = followerRepository.save(followerEntity);

        return ApiResponse.<FollowerResDTO>builder()
                .status(201)
                .success(true)
                .data(convertToFollowerResDTO(savedFollower))
                .message("Follow request sent successfully")
                .build();
    }

    @Override
    public ApiResponse<String> approveFriendship(Long followerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }
        String loggedInUserEmail = authentication.getName();
        User user  = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Follower follower = followerRepository.findById(followerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Follower relationship not found"));

        if ("Friends".equals(follower.getRelationshipType().getTypeName())) {
            return ApiResponse.<String>builder()
                    .status(400)
                    .success(false)
                    .message("You are already friends")
                    .build();
        }

        if ("Pending".equals(follower.getRelationshipType().getTypeName())) {
            RelationshipType relationshipType = relationshipTypeRepository.findByTypeName("Friends")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relationship type not found"));

            follower.setRelationshipType(relationshipType);
            follower.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            followerRepository.save(follower);

            return ApiResponse.<String>builder()
                    .status(200)
                    .success(true)
                    .message("Friendship approved")
                    .build();
        }

        return ApiResponse.<String>builder()
                .status(400)
                .success(false)
                .message("Invalid relationship type")
                .build();
    }

    @Override
    public ApiResponse<List<FriendResDTO>> getFriends() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }

        String loggedInUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        RelationshipType relationshipType = relationshipTypeRepository.findByTypeName("Friends")
                .orElseThrow(() -> new RuntimeException("Relationship type not found"));

        List<Follower> followers = followerRepository.findByFollowedIdOrFollowerIdAndRelationshipType(
                currentUser.getId(), currentUser.getId(), relationshipType);

        List<FriendResDTO> result = followers.stream()
                .map(follower -> {
                    // Determine which user to return (the one that is not the current user)
                    User friendUser;
                    if (follower.getFollower().getId().equals(currentUser.getId())) {
                        friendUser = follower.getFollowed();
                    } else {
                        friendUser = follower.getFollower();
                    }

                    return FriendResDTO.builder()
                            .id(follower.getId())
                            .userId(friendUser.getId())
                            .username(friendUser.getFull_name())
                            .email(friendUser.getEmail())
                            // If you have an avatar field in your User entity, uncomment and use it
                            // .avatar(friendUser.getAvatar())
                            .relationshipType(follower.getRelationshipType().getTypeName())
                            .build();
                })
                .collect(Collectors.toList());

        return ApiResponse.<List<FriendResDTO>>builder()
                .status(200)
                .success(true)
                .data(result)
                .message("Friends retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<List<FriendResDTO>> getFollowRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }

        String loggedInUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        RelationshipType relationshipType = relationshipTypeRepository.findByTypeName("Pending")
                .orElseThrow(() -> new RuntimeException("Relationship type not found"));

        // Lấy danh sách yêu cầu follow mà currentUser là người nhận (followedId)
        List<Follower> requests = followerRepository.findByFollowedIdAndRelationshipType(currentUser.getId(), relationshipType);

        List<FriendResDTO> result = requests.stream()
                .map(follower -> {
                    User requester = follower.getFollower(); // Người gửi yêu cầu

                    return FriendResDTO.builder()
                            .id(follower.getId())   // ID của yêu cầu follow
                            .userId(requester.getId()) // ID của người gửi yêu cầu
                            .username(requester.getFull_name()) // Tên người gửi yêu cầu
                            .email(requester.getEmail()) // Email của người gửi yêu cầu
//                            .avatar(requester.getAvatar()) // Ảnh đại diện (nếu có)
                            .relationshipType(follower.getRelationshipType().getTypeName()) // Loại quan hệ ("Pending")
                            .build();
                })
                .collect(Collectors.toList());

        return ApiResponse.<List<FriendResDTO>>builder()
                .status(200)
                .success(true)
                .data(result)
                .message("Follow requests retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<String> removeFriendship(FollowerApprovalReqDTO approvalReqDTO) {
        Follower follower = followerRepository.findByFollowerIdAndFollowedId(approvalReqDTO.getFollowerId(), approvalReqDTO.getFollowedId())
                .orElseThrow(() -> new RuntimeException("Follower relationship not found"));

        followerRepository.delete(follower);
        return ApiResponse.<String>builder()
                .status(200)
                .success(true)
                .message("Friendship removed")
                .build();
    }

    @Override
    public ApiResponse<String> getFriendshipStatus(Long followerId, Long followedId) {
        Follower follower = followerRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> new RuntimeException("Follower relationship not found"));
        String relationshipTypeName = follower.getRelationshipType().getTypeName();

        if ("Pending".equals(relationshipTypeName)) {
            return ApiResponse.<String>builder()
                    .status(200)
                    .success(true)
                    .message("Friendship status: Pending")
                    .build();
        } else if ("Friends".equals(relationshipTypeName)) {
            return ApiResponse.<String>builder()
                    .status(200)
                    .success(true)
                    .message("Friendship status: Friends")
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .status(400)
                    .success(false)
                    .message("Unknown relationship status")
                    .build();
        }
    }

    private FollowerResDTO convertToFollowerResDTO(Follower follower) {
        return FollowerResDTO.builder()
                .id(follower.getId())
                .followerId(follower.getFollower().getId())
                .followedId(follower.getFollowed().getId())
                .relationshipType(follower.getRelationshipType().getId())
                .createdAt(follower.getCreated_at())
                .updatedAt(follower.getUpdated_at())
                .build();
    }
}
