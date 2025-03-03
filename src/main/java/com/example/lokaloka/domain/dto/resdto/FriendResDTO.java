package com.example.lokaloka.domain.dto.resdto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendResDTO {
    private Long id;           // Follower relationship ID
    private Long userId;       // ID of the friend (not the current user)
    private String username;   // Username of the friend
    private String email;      // Email of the friend
    private String avatar;     // Avatar of the friend
    private String relationshipType; // Type of relationship (e.g., "Friends")
}
