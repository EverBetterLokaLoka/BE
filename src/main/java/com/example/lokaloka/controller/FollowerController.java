package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.FollowerApprovalReqDTO;
import com.example.lokaloka.domain.dto.reqdto.FollowerReqDTO;
import com.example.lokaloka.domain.dto.resdto.FollowerResDTO;
import com.example.lokaloka.domain.dto.resdto.FriendResDTO;
import com.example.lokaloka.service.IFollowerService;
import com.example.lokaloka.util.ApiResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowerController {

    private IFollowerService followerService;

    // Tạo kết bạn
    @PostMapping
    public ResponseEntity<ApiResponse<FollowerResDTO>> createFriendship(@RequestBody FollowerReqDTO followerReqDTO) {
        ApiResponse<FollowerResDTO> response = followerService.createFriendship(followerReqDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/approval/{id}")
    public ResponseEntity<ApiResponse<String>> approveFriendship(@PathVariable Long id) {
        ApiResponse<String> response = followerService.approveFriendship(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Lấy danh sách bạn bè
    @GetMapping()
    public ResponseEntity<ApiResponse<List<FriendResDTO>>> getFriends() {
        ApiResponse<List<FriendResDTO>>  response = followerService.getFriends();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Xóa kết bạn
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteFriendship(@RequestBody FollowerApprovalReqDTO approvalReqDTO) {
        ApiResponse<String> response = followerService.removeFriendship(approvalReqDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Lấy trạng thái kết bạn
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getFriendshipStatus(@RequestParam Long followerId, @RequestParam Long followedId) {
        ApiResponse<String> response = followerService.getFriendshipStatus(followerId, followedId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Lấy danh sách yêu cầu kết bạn
    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<List<FriendResDTO>>> getFollowRequests() {
        ApiResponse<List<FriendResDTO>> response = followerService.getFollowRequests();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
