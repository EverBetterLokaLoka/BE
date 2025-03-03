package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.FollowerApprovalReqDTO;
import com.example.lokaloka.domain.dto.reqdto.FollowerReqDTO;
import com.example.lokaloka.domain.dto.resdto.FollowerResDTO;
import com.example.lokaloka.domain.dto.resdto.FriendResDTO;
import com.example.lokaloka.util.ApiResponse;

import java.util.List;

public interface IFollowerService {
    ApiResponse<FollowerResDTO> createFriendship(FollowerReqDTO followerReqDTO);
    ApiResponse<String> approveFriendship(Long FollowerId);
    ApiResponse<List<FriendResDTO>>  getFriends();
    ApiResponse<String> removeFriendship(FollowerApprovalReqDTO approvalReqDTO);
    ApiResponse<String> getFriendshipStatus(Long followerId, Long followedId);
    ApiResponse<List<FriendResDTO>>getFollowRequests();
}
