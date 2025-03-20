package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.FollowerApprovalReqDTO;
import com.example.lokaloka.domain.dto.reqdto.FollowerReqDTO;
import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.dto.resdto.FollowerResDTO;
import com.example.lokaloka.domain.dto.resdto.FriendResDTO;
import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import com.example.lokaloka.service.IFollowerService;
import com.example.lokaloka.util.ApiResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    public ResponseEntity<ApiResponse<String>> deleteFriendship(@RequestParam Long friendId) {
        ApiResponse<String> response = followerService.removeFriendship(friendId);
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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FriendResDTO>>> searchUsers(@RequestParam String keyword) {
        ApiResponse<List<FriendResDTO>> response = followerService.searchUsers(keyword);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/unfriend") // Đường dẫn cho API gỡ kết bạn
    public ResponseEntity<ApiResponse<String>> unfriend(@RequestParam Long friendId) {
        ApiResponse<String> response = followerService.removeFriendship(friendId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<ApiResponse<List<FriendResDTO>>> getPendingRequests() {
        ApiResponse<List<FriendResDTO>> response = followerService.getPendingRequests();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/cancel-request/{id}")
    public ResponseEntity<ApiResponse<String>> cancelFriendRequest(@PathVariable Long id) {
        ApiResponse<String> response = followerService.cancelFriendRequest(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/cancel-request-send/{id}")
    public ResponseEntity<ApiResponse<String>> cancelFriendRequestSend(@PathVariable Long id) {
        ApiResponse<String> response = followerService.cancelFriendRequestSend(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
