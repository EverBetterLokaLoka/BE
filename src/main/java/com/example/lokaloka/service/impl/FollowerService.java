package com.example.lokaloka.service.impl;

import com.example.lokaloka.controller.NotificationWebSocketController;
import com.example.lokaloka.domain.dto.reqdto.FollowerApprovalReqDTO;
import com.example.lokaloka.domain.dto.reqdto.FollowerReqDTO;
import com.example.lokaloka.domain.dto.reqdto.NotificationReqDTO;
import com.example.lokaloka.domain.dto.resdto.FollowerResDTO;
import com.example.lokaloka.domain.dto.resdto.FriendResDTO;
import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import com.example.lokaloka.domain.entity.Follower;
import com.example.lokaloka.domain.entity.RelationshipType;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.IFollowerRepository;
import com.example.lokaloka.repository.IRelationshipTypeRepository;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.service.IFollowerService;
import com.example.lokaloka.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@Slf4j
@Service
public class FollowerService implements IFollowerService {

    @Autowired
    private IFollowerRepository followerRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRelationshipTypeRepository relationshipTypeRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationWebSocketController notificationWebSocketController; // Thêm vào đây

//    @Override
//    public ApiResponse<FollowerResDTO> createFriendship(FollowerReqDTO followerReqDTO) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
//        }
//
//        String loggedInUserEmail = authentication.getName();
//
//        User follower = userRepository.findByEmail(loggedInUserEmail)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        User followed = userRepository.findById(followerReqDTO.getFollowedId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Followed user not found"));
//
//        if (follower.getId().equals(followed.getId())) {
//            return ApiResponse.<FollowerResDTO>builder()
//                    .status(400)
//                    .success(false)
//                    .message("You cannot follow yourself")
//                    .build();
//        }
//
//        RelationshipType pendingRelationshipType = relationshipTypeRepository.findByTypeName("Pending")
//                .orElseThrow(() -> new RuntimeException("Relationship type not found"));
//
//        // Kiểm tra mối quan hệ "Friends" giữa follower và followed
//        List<Follower> existingFriendship1 = followerRepository.findByFollowerAndFollowed(follower, followed);
//        List<Follower> existingFriendship2 = followerRepository.findByFollowerAndFollowed(followed, follower);
//
//        if (!existingFriendship1.isEmpty() || !existingFriendship2.isEmpty()) {
//            return ApiResponse.<FollowerResDTO>builder()
//                    .status(400)
//                    .success(false)
//                    .message("You are already friends with this user.")
//                    .build();
//        }
//
//        // Kiểm tra xem đã tồn tại yêu cầu "Pending" không
//        List<Follower> existingPendingFollowers = followerRepository.findByFollowerAndFollowedAndRelationshipType(follower, followed, pendingRelationshipType);
//        if (!existingPendingFollowers.isEmpty()) {
//            return ApiResponse.<FollowerResDTO>builder()
//                    .status(400)
//                    .success(false)
//                    .message("You have already sent a follow request to this user.")
//                    .build();
//        }
//
//        // Tạo yêu cầu theo dõi mới
//        Follower followerEntity = new Follower();
//        followerEntity.setFollower(follower);
//        followerEntity.setFollowed(followed);
//        followerEntity.setRelationshipType(pendingRelationshipType);
//        followerEntity.setCreated_at(new Timestamp(System.currentTimeMillis()));
//        followerEntity.setUpdated_at(new Timestamp(System.currentTimeMillis()));
//
//        Follower savedFollower = followerRepository.save(followerEntity);
//        messagingTemplate.convertAndSend("/topic/friendship",
//                "User " + follower.getFull_name() + " sent you a friend request!");
//
//        return ApiResponse.<FollowerResDTO>builder()
//                .status(201)
//                .success(true)
//                .data(convertToFollowerResDTO(savedFollower))
//                .message("Follow request sent successfully")
//                .build();
//    }

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

        RelationshipType pendingRelationshipType = relationshipTypeRepository.findByTypeName("Pending")
                .orElseThrow(() -> new RuntimeException("Relationship type not found"));

        // Kiểm tra mối quan hệ "Friends" giữa follower và followed
        List<Follower> existingFriendship1 = followerRepository.findByFollowerAndFollowed(follower, followed);
        List<Follower> existingFriendship2 = followerRepository.findByFollowerAndFollowed(followed, follower);

        if (!existingFriendship1.isEmpty() || !existingFriendship2.isEmpty()) {
            return ApiResponse.<FollowerResDTO>builder()
                    .status(400)
                    .success(false)
                    .message("You are already friends with this user.")
                    .build();
        }

        // Kiểm tra xem đã tồn tại yêu cầu "Pending" không
        List<Follower> existingPendingFollowers = followerRepository.findByFollowerAndFollowedAndRelationshipType(follower, followed, pendingRelationshipType);
        if (!existingPendingFollowers.isEmpty()) {
            return ApiResponse.<FollowerResDTO>builder()
                    .status(400)
                    .success(false)
                    .message("You have already sent a follow request to this user.")
                    .build();
        }

        // Tạo yêu cầu theo dõi mới
        Follower followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setFollowed(followed);
        followerEntity.setRelationshipType(pendingRelationshipType);
        followerEntity.setCreated_at(new Timestamp(System.currentTimeMillis()));
        followerEntity.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        Follower savedFollower = followerRepository.save(followerEntity);

        // 🔹 **Tạo thông báo trong cơ sở dữ liệu trước**
        String notificationMessage = "User " + follower.getFull_name() + " sent you a friend request.";
        NotificationReqDTO notificationReqDTO = new NotificationReqDTO(followed.getId(), notificationMessage, follower.getId());
        notificationService.createNotification(notificationReqDTO);

        // 🔹 **Gửi thông báo qua WebSocket**
        log.info("📢 Sending WebSocket message to user: " + followed.getId());

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "Friend Request");
        payload.put("body", notificationMessage);
        payload.put("senderId", follower.getId());
        payload.put("type", "FRIEND_REQUEST");
        payload.put("friendId", savedFollower.getId());

//        messagingTemplate.convertAndSendToUser(
//                followed.getId().toString(),
//                "/queue/notifications",
//                payload
//        );
        String destination = String.format("/topic/notifications/%s", followed.getId());
        messagingTemplate.convertAndSend(destination, payload);

        log.info("✅ Message sent via WebSocket: " + payload);


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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not authenticated");
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

            messagingTemplate.convertAndSend("/topic/friendship",
                    "User " + user.getFull_name() + " accepted your friend request!");
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

        // Lấy tất cả các mối quan hệFriends
        List<Follower> followers = followerRepository.findByFollowedIdOrFollowerId(currentUser.getId(), currentUser.getId());

        // Lọc chỉ lấy các mối quan hệ "Friends"
        List<FriendResDTO> result = followers.stream()
                .filter(follower -> follower.getRelationshipType().getTypeName().equals("Friends")) // Lọc chỉ lấy "Friends"
                .map(follower -> {
                    // Xác định người bạn để trả về (người không phải là người dùng hiện tại)
                    User friendUser = follower.getFollower().getId().equals(currentUser.getId())
                            ? follower.getFollowed()
                            : follower.getFollower();

                    return FriendResDTO.builder()
                            .id(follower.getId())
                            .userId(friendUser.getId())
                            .username(friendUser.getFull_name())
                            .email(friendUser.getEmail())
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

//        List<Follower> followers = followerRepository.findByFollowedIdOrFollowerIdAndRelationshipType(
//                currentUser.getId(), currentUser.getId(), relationshipType);

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
    public ApiResponse<String> removeFriendship(Long friendshipId) {
        Follower follower = followerRepository.findById(friendshipId)
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

    @Override
    public ApiResponse<List<FriendResDTO>> searchUsers(String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }

        // Giả sử bạn lấy email hoặc id của người dùng đang xác thực
        String currentUserEmail = authentication.getName(); // Hoặc dùng "getPrincipal()" để lấy thông tin người dùng
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);

        List<User> users = userRepository.searchByFullName(keyword);

        // Lọc kết quả để loại bỏ bản thân người dùng đang xác thực
        List<FriendResDTO> result = users.stream()
                .filter(user -> !user.getEmail().equals(currentUserEmail)) // So sánh email
                .map(user -> FriendResDTO.builder()
                        .id(user.getId())
                        .userId(user.getId())
                        .username(user.getFull_name())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.<List<FriendResDTO>>builder()
                .status(200)
                .success(true)
                .data(result)
                .message("Users retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<List<FriendResDTO>> getPendingRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }

        String loggedInUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        RelationshipType pendingRelationshipType = relationshipTypeRepository.findByTypeName("Pending")
                .orElseThrow(() -> new RuntimeException("Relationship type not found"));

        // Lấy danh sách các yêu cầu chưa được chấp thuận
        List<Follower> pendingRequests = followerRepository.findByFollowerAndRelationshipType(currentUser, pendingRelationshipType);

        // Chuyển đổi thành List<FriendResDTO>
        List<FriendResDTO> result = pendingRequests.stream()
                .map(follower -> {
                    User followedUser = follower.getFollowed(); // Người được gửi yêu cầu
                    return FriendResDTO.builder()
                            .id(follower.getId())
                            .userId(followedUser.getId())
                            .username(followedUser.getFull_name())
                            .email(followedUser.getEmail())
                            .relationshipType(follower.getRelationshipType().getTypeName())
                            .build();
                })
                .collect(Collectors.toList());

        return ApiResponse.<List<FriendResDTO>>builder()
                .status(200)
                .success(true)
                .data(result)
                .message("Pending requests retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<String> cancelFriendRequest(Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not authenticated");
        }

        String loggedInUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Follower follower = followerRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow request not found"));

        if (!follower.getFollowed().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "This request does not belong to the current user");
        }

        // Kiểm tra loại mối quan hệ
        if (!follower.getRelationshipType().getTypeName().equals("Pending")) {
            return ApiResponse.<String>builder()
                    .status(400)
                    .success(false)
                    .message("Cannot cancel a request that is not pending")
                    .build();
        }

        // Xóa yêu cầu kết bạn
        followerRepository.delete(follower);

        return ApiResponse.<String>builder()
                .status(200)
                .success(true)
                .message("Friend request canceled successfully")
                .build();
    }

    @Override
    public ApiResponse<String> cancelFriendRequestSend(Long followedId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not authenticated");
        }

        String loggedInUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Tìm yêu cầu kết bạn có trạng thái "Pending", follower là currentUser và followed là followedId
        Follower follower = followerRepository.findPendingRequest(currentUser.getId(), followedId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow request not found or not pending"));

        followerRepository.delete(follower);


        return ApiResponse.<String>builder()
                .status(200)
                .success(true)
                .message("Friend request canceled successfully")
                .build();
    }

}
